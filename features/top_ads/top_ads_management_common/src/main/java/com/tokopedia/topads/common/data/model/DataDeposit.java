package com.tokopedia.topads.common.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hadi.putra on 23/04/18.
 */

public class DataDeposit {
    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("amount_fmt")
    @Expose
    private String amountFmt;
    @SerializedName("ad_usage")
    @Expose
    private boolean adUsage;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAmountFmt() {
        return amountFmt;
    }

    public void setAmountFmt(String amountFmt) {
        this.amountFmt = amountFmt;
    }

    public boolean isAdUsage() {
        return adUsage;
    }

    public void setAdUsage(boolean adUsage) {
        this.adUsage = adUsage;
    }
}
