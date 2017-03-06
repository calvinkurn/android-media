package com.tokopedia.digital.cart.model;

/**
 * Created by Nabilla Sabbaha on 3/1/2017.
 */

public class UserInputPriceDigital {

    private String minPayment;

    private String maxPayment;

    private long minPaymentPlain;

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
}
