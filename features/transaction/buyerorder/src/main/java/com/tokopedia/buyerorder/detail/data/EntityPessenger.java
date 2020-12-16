package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.SerializedName;

public class EntityPessenger {

    @SerializedName("element_type")
    private String elementType;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("help_text")
    private String helpText;

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("required")
    private int require;

    @SerializedName("title")
    private String title;

    @SerializedName("validator_regex")
    private String validatorRegex;

    @SerializedName("value")
    private String value;

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRequire() {
        return require;
    }

    public void setRequire(int require) {
        this.require = require;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValidatorRegex() {
        return validatorRegex;
    }

    public void setValidatorRegex(String validatorRegex) {
        this.validatorRegex = validatorRegex;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
