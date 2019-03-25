package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class CouponUpperLeftSection {
    @SerializedName("backgroundColor")
    private String backgroundColor;

    @SerializedName("textAttributes")
    Map<String, String> textAttributes;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Map<String, String> getTextAttributes() {
        return textAttributes;
    }

    public void setTextAttributes(Map<String, String> textAttributes) {
        this.textAttributes = textAttributes;
    }
}
