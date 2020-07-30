package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class OrderToken {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("QRCodeUrl")
    @Expose
    private String QRCodeUrl;

    public OrderToken(String label, String value, String QRCodeUrl) {
        this.label = label;
        this.value = value;
        this.QRCodeUrl = QRCodeUrl;
    }

    @Override
    public String toString() {
        return "[OrderToken:{ "
                + "label="+label +" "
                + "value="+value +" "
                + "QRCodeUrl="+QRCodeUrl
                + "}]";
    }
}