package com.tokopedia.tkpd.thankyou.data.pojo.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;

/**
 * Created by okasurya on 12/7/17.
 */

public class PaymentGraphql {
    @SerializedName("payment")
    @Expose
    private PaymentData payment;

    public PaymentData getPayment() {
        return payment;
    }

    public void setPayment(PaymentData payment) {
        this.payment = payment;
    }
}
