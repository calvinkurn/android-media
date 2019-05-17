package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class CouponUpperLeftSection {
    @SerializedName("backgroundColor")
    private String backgroundColor;

    @SerializedName("textAttributes")
    private List<TextAttributes> textAttributes;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<TextAttributes> getTextAttributes() {
        return textAttributes;
    }

    public void setTextAttributes(List<TextAttributes> textAttributes) {
        this.textAttributes = textAttributes;
    }
}
