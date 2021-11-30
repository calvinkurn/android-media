package com.tokopedia.buyerorder.detail.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by baghira on 11/05/18.
 */

public class Invoice {
    @SerializedName("invoiceRefNum")
    @Expose
    private String invoiceRefNum;
    @SerializedName("invoiceUrl")
    @Expose
    private String invoiceUrl;

    public String invoiceRefNum() {
        return invoiceRefNum;
    }

    public String invoiceUrl() {
        return invoiceUrl;
    }

    @Override
    public String toString() {
        return "[Invoice:{"
                + "invoiceRefNum="+invoiceRefNum +" "
                + "invoiceUrl="+invoiceUrl
                + "}]";
    }
}