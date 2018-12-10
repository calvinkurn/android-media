package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getMaxPayment() {
        return maxPayment;
    }

    public long getMinPaymentPlain() {
        return minPaymentPlain;
    }

    public long getMaxPaymentPlain() {
        return maxPaymentPlain;
    }
}
