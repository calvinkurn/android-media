package com.tokopedia.paymentmanagementsystem.paymentlist.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class DataPaymentList {

    @SerializedName("paymentList")
    @Expose
    private PaymentList paymentList = null;

    public PaymentList getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(PaymentList paymentList) {
        this.paymentList = paymentList;
    }
}
