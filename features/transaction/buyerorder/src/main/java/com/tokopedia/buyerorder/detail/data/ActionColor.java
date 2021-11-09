package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActionColor implements Serializable {
    @SerializedName("textColor")
    @Expose
    private String textColor;

    @SerializedName("border")
    @Expose
    private String border;

    @SerializedName("background")
    @Expose
    private String background;

    public String getTextColor() {
        return textColor;
    }

    public String getBorder() {
        return border;
    }

    public String getBackground() {
        return background;
    }
}

