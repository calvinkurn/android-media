package com.tokopedia.buyerorder.list.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("textColor")
    @Expose
    private String textColor;
    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;

    public MetaData(String label, String value, String textColor, String backgroundColor) {
        this.label = label;
        this.value = value;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    public String textColor() {
        return textColor;
    }

    public String backgroundColor() {
        return backgroundColor;
    }

    @Override
    public String toString() {
        return "MetaData{"
                + "label=" + label + ", "
                + "value=" + value + ", "
                + "textColor=" + textColor + ", "
                + "backgroundColor=" + backgroundColor
                + "}";
    }
}
