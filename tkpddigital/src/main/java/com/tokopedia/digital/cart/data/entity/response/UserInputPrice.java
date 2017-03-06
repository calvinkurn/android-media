package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class UserInputPrice {

    @SerializedName("min_payment")
    @Expose
    private String minPayment;
    @SerializedName("max_payment")
    @Expose
    private String maxPayment;
    @SerializedName("min_payment_plain")
    @Expose
    private long minPaymentPlain;
    @SerializedName("max_payment_plain")
    @Expose
    private long maxPaymentPlain;

    public String getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(String minPayment) {
        this.minPayment = minPayment;
    }

    public String getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(String maxPayment) {
        this.maxPayment = maxPayment;
    }

    public long getMinPaymentPlain() {
        return minPaymentPlain;
    }

    public void setMinPaymentPlain(long minPaymentPlain) {
        this.minPaymentPlain = minPaymentPlain;
    }

    public long getMaxPaymentPlain() {
        return maxPaymentPlain;
    }

    public void setMaxPaymentPlain(long maxPaymentPlain) {
        this.maxPaymentPlain = maxPaymentPlain;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
