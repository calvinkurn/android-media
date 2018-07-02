
package com.tokopedia.paymentmanagementsystem.paymentlist.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentList {

    @SerializedName("payment_list")
    @Expose
    private List<PaymentListInside> paymentList = null;

    public List<PaymentListInside> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentListInside> paymentList) {
        this.paymentList = paymentList;
    }

}
