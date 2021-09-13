package com.tokopedia.buyerorder.detail.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentData {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("textColor")
    @Expose
    private String textColor;

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    public String textColor() {
        return textColor;
    }

    @Override
    public String toString() {
        return "PaymentData{"
                + "label=" + label + ", "
                + "value=" + value + ", "
                + "textColor=" + textColor
                + "}";
    }
}
