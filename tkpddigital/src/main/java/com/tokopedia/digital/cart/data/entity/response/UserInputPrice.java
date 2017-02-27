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
    private long minPayment;
    @SerializedName("max_payment")
    @Expose
    private long maxPayment;

    public long getMinPayment() {
        return minPayment;
    }

    public void setMinPayment(long minPayment) {
        this.minPayment = minPayment;
    }

    public long getMaxPayment() {
        return maxPayment;
    }

    public void setMaxPayment(long maxPayment) {
        this.maxPayment = maxPayment;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
