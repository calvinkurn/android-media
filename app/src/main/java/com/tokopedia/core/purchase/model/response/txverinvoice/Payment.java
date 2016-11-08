package com.tokopedia.core.purchase.model.response.txverinvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class Payment {
    private static final String TAG = Payment.class.getSimpleName();

    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;
    @SerializedName("payment_ref")
    @Expose
    private String paymentRef;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }
}
