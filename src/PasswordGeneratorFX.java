import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.security.SecureRandom;

public class PasswordGeneratorFX extends Application {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()-_+=<>?";

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Longitud de la contraseña:");
        Slider slider = new Slider(6, 30, 12);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        CheckBox chkUppercase = new CheckBox("Mayúsculas");
        CheckBox chkNumbers = new CheckBox("Números");
        CheckBox chkSpecials = new CheckBox("Caracteres especiales");
        Button generateBtn = new Button("Generar");
        TextField passwordField = new TextField();
        passwordField.setEditable(false);

        Text strengthIndicator = new Text("Fortaleza: -");
        strengthIndicator.setFill(Color.GRAY);

        Button copyBtn = new Button("Copiar");
        copyBtn.setDisable(true);

        generateBtn.setOnAction(e -> {
            int length = (int) slider.getValue();
            boolean useUpper = chkUppercase.isSelected();
            boolean useNumbers = chkNumbers.isSelected();
            boolean useSpecials = chkSpecials.isSelected();
            String password = generatePassword(length, useUpper, useNumbers, useSpecials);
            passwordField.setText(password);
            evaluateStrength(password, strengthIndicator);
            copyBtn.setDisable(false);
        });

        copyBtn.setOnAction(e -> {
            final String password = passwordField.getText();
            if (!password.isEmpty()) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(password);
                clipboard.setContent(content);
            }
        });

        VBox layout = new VBox(10, label, slider, chkUppercase, chkNumbers, chkSpecials, generateBtn, passwordField, strengthIndicator, copyBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");
        Scene scene = new Scene(layout, 350, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Generador de Contraseñas");
        primaryStage.show();
    }

    private String generatePassword(int length, boolean useUpper, boolean useNumbers, boolean useSpecials) {
        String base = LOWERCASE;
        if (useUpper) base += UPPERCASE;
        if (useNumbers) base += NUMBERS;
        if (useSpecials) base += SPECIALS;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(base.charAt(random.nextInt(base.length())));
        }
        return password.toString();
    }

    private void evaluateStrength(String password, Text indicator) {
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*()-_+=<>?].*")) score++;

        switch (score) {
            case 0 -> {
                indicator.setText("Fortaleza: Muy Débil");
                indicator.setFill(Color.RED);
            }
            case 1 -> {
                indicator.setText("Fortaleza: Débil");
                indicator.setFill(Color.ORANGE);
            }
            case 2 -> {
                indicator.setText("Fortaleza: Media");
                indicator.setFill(Color.YELLOW);
            }
            case 3 -> {
                indicator.setText("Fortaleza: Fuerte");
                indicator.setFill(Color.GREEN);
            }
            case 4 -> {
                indicator.setText("Fortaleza: Muy Fuerte");
                indicator.setFill(Color.BLUE);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}