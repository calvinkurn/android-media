package com.tokopedia.posapp.data.pojo.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 10/10/17.
 */

public class PaymentDetail {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("amount")
    @Expose
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
