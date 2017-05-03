
package com.tokopedia.core.product.model.shopscore.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SumData {

    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Value")
    @Expose
    private Integer value;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
