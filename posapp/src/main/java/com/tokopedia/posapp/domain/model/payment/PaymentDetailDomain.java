package com.tokopedia.posapp.domain.model.payment;

/**
 * Created by okasurya on 10/11/17.
 */

public class PaymentDetailDomain {
    private String name;
    private double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
