package com.tokopedia.buyerorder.list.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.buyerorder.detail.data.Color;

public class ConditionalInfo {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("color")
    @Expose
    private Color color;

    ConditionalInfo(String text, Color color) {
        this.text = text;
        this.color = color;
    }

    public String text() {
        return text;
    }

    public Color color() {
        return color;
    }

    @Override
    public String toString() {
        return "ConditionalInfo{"
                + "text=" + text + ", "
                + "color=" + color
                + "}";
    }
}
