package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.SerializedName;

public class TextAttributes {
    @SerializedName("text")
    private String text;

    @SerializedName("color")
    private String color;

    @SerializedName("isBold")
    private boolean isBold;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
