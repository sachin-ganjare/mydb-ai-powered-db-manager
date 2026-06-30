package com.mydb.controllers;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class MySQLDocsController {

    private static final String DEFAULT_DOCS_URL = "https://dev.mysql.com/doc/";

    public BorderPane createDocsView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("docs-root");

        Label title = new Label("MySQL Documentation, Tips & Patterns");
        title.getStyleClass().add("docs-title");

        WebView webView = new WebView();
        webView.setPrefHeight(520);
        VBox.setVgrow(webView, Priority.ALWAYS);

        WebEngine webEngine = webView.getEngine();
        TextField addressField = new TextField(DEFAULT_DOCS_URL);
        addressField.getStyleClass().add("docs-address-field");
        addressField.setOnAction(event -> loadUrl(webEngine, addressField.getText()));

        Button homeButton = createToolbarButton("Docs Home", FontAwesomeSolid.HOME);
        homeButton.setOnAction(event -> {
            addressField.setText(DEFAULT_DOCS_URL);
            webEngine.load(DEFAULT_DOCS_URL);
        });

        Button reloadButton = createToolbarButton("Reload", FontAwesomeSolid.SYNC);
        reloadButton.setOnAction(event -> webEngine.reload());

        Button goButton = createToolbarButton("Go", FontAwesomeSolid.ARROW_RIGHT);
        goButton.setOnAction(event -> loadUrl(webEngine, addressField.getText()));

        ToolBar docsToolbar = new ToolBar(homeButton, reloadButton, addressField, goButton);
        docsToolbar.getStyleClass().add("docs-toolbar");

        VBox header = new VBox(10, title, docsToolbar);
        header.setPadding(new Insets(15, 15, 10, 15));
        root.setTop(header);

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                addressField.setText(newValue);
            }
        });

        webEngine.load(DEFAULT_DOCS_URL);

        VBox tipsBox = new VBox(12);
        tipsBox.getStyleClass().add("docs-tips");
        Label tipsHeader = new Label("MySQL Tricks & Common Patterns");
        tipsHeader.getStyleClass().add("docs-tips-title");

        Label tipsContent = new Label(
                "- Use EXPLAIN to analyze query execution plans.\n" +
                "- Add indexes for columns used in WHERE, JOIN, and ORDER BY clauses.\n" +
                "- Avoid SELECT * in application queries; select only the columns you need.\n" +
                "- Use prepared statements to reduce SQL injection risk.\n" +
                "- Wrap related writes in transactions with START TRANSACTION, COMMIT, and ROLLBACK.\n" +
                "- Use INFORMATION_SCHEMA for metadata queries.\n" +
                "- Use LIMIT and OFFSET for pagination.\n" +
                "- Filter and aggregate early to avoid very large result sets.\n" +
                "- Use stored procedures and functions when reusable database-side logic helps.");
        tipsContent.getStyleClass().add("docs-tips-content");
        tipsContent.setWrapText(true);
        tipsBox.getChildren().addAll(tipsHeader, tipsContent);

        ScrollPane tipsScroll = new ScrollPane(tipsBox);
        tipsScroll.setPrefHeight(280);
        tipsScroll.setFitToWidth(true);

        VBox centerBox = new VBox(10, webView, tipsScroll);
        centerBox.setPadding(new Insets(0, 15, 15, 15));
        root.setCenter(centerBox);

        return root;
    }

    private Button createToolbarButton(String text, FontAwesomeSolid iconType) {
        Button button = new Button(text);
        button.setGraphic(new FontIcon(iconType));
        button.getStyleClass().add("icon-button");
        return button;
    }

    private void loadUrl(WebEngine webEngine, String rawUrl) {
        String url = rawUrl == null ? "" : rawUrl.trim();
        if (url.isEmpty()) {
            url = DEFAULT_DOCS_URL;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        webEngine.load(url);
    }
}
