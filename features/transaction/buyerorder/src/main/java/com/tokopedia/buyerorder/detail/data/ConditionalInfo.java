package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConditionalInfo {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("color")
    @Expose
    private Color color;

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
