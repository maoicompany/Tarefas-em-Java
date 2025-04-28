import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MiniWordJava extends JFrame {

    private JTextPane textPane;

    public MiniWordJava() {
        setTitle("Mini Word Java");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);

        JToolBar toolBar = new JToolBar();

        JButton boldButton = new JButton("Negrito");
        boldButton.addActionListener(e -> toggleStyle(StyleConstants.Bold));
        toolBar.add(boldButton);

        JButton italicButton = new JButton("Itálico");
        italicButton.addActionListener(e -> toggleStyle(StyleConstants.Italic));
        toolBar.add(italicButton);

        JButton underlineButton = new JButton("Sublinhado");
        underlineButton.addActionListener(e -> toggleStyle(StyleConstants.Underline));
        toolBar.add(underlineButton);

        JButton strikeButton = new JButton("Tachado");
        strikeButton.addActionListener(e -> toggleStyle(StyleConstants.StrikeThrough));
        toolBar.add(strikeButton);

        JButton biggerFontButton = new JButton("Aumentar Fonte");
        biggerFontButton.addActionListener(e -> changeFontSize(2));
        toolBar.add(biggerFontButton);

        JButton smallerFontButton = new JButton("Diminuir Fonte");
        smallerFontButton.addActionListener(e -> changeFontSize(-2));
        toolBar.add(smallerFontButton);

        JButton colorButton = new JButton("Cor");
        colorButton.addActionListener(e -> changeColor());
        toolBar.add(colorButton);

        JButton deleteButton = new JButton("Excluir");
        deleteButton.addActionListener(e -> deleteSelectedText());
        toolBar.add(deleteButton);

        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(e -> saveToFile());
        toolBar.add(saveButton);

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void toggleStyle(Object style) {
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) {
            return; // Nada selecionado
        }

        Element element = doc.getCharacterElement(start);
        AttributeSet as = element.getAttributes();

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
        boolean isSet = StyleConstants.isBold(as) && style == StyleConstants.Bold
                     || StyleConstants.isItalic(as) && style == StyleConstants.Italic
                     || StyleConstants.isUnderline(as) && style == StyleConstants.Underline
                     || StyleConstants.isStrikeThrough(as) && style == StyleConstants.StrikeThrough;

        if (style == StyleConstants.Bold) {
            StyleConstants.setBold(asNew, !isSet);
        } else if (style == StyleConstants.Italic) {
            StyleConstants.setItalic(asNew, !isSet);
        } else if (style == StyleConstants.Underline) {
            StyleConstants.setUnderline(asNew, !isSet);
        } else if (style == StyleConstants.StrikeThrough) {
            StyleConstants.setStrikeThrough(asNew, !isSet);
        }

        doc.setCharacterAttributes(start, end - start, asNew, true);
    }

    private void changeFontSize(int increment) {
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) {
            return;
        }

        Element element = doc.getCharacterElement(start);
        AttributeSet as = element.getAttributes();
        int size = StyleConstants.getFontSize(as) + increment;

        if (size < 2) size = 2;

        MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
        StyleConstants.setFontSize(asNew, size);
        doc.setCharacterAttributes(start, end - start, asNew, true);
    }

    private void changeColor() {
        Color color = JColorChooser.showDialog(this, "Escolher Cor", Color.BLACK);
        if (color != null) {
            StyledDocument doc = textPane.getStyledDocument();
            int start = textPane.getSelectionStart();
            int end = textPane.getSelectionEnd();

            if (start == end) {
                return;
            }

            MutableAttributeSet asNew = new SimpleAttributeSet();
            StyleConstants.setForeground(asNew, color);
            doc.setCharacterAttributes(start, end - start, asNew, false);
        }
    }

    private void deleteSelectedText() {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start != end) {
            try {
                textPane.getDocument().remove(start, end - start);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Garante que o arquivo tenha a extensão .txt
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textPane.getText());
                JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MiniWordJava().setVisible(true);
        });
    }
}
