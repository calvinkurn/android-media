package com.tokopedia.pms.paymentlist.domain.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
