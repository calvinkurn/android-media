package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 21/09/18.
 */
public class PendingCashbackModel {

    @SerializedName("amount")
    @Expose
    private Integer amount = 0;
    @SerializedName("amount_text")
    @Expose
    private String amountText = "";
    @SerializedName("currency")
    @Expose
    private String currency = "";

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAmountText() {
        return amountText;
    }

    public void setAmountText(String amountText) {
        this.amountText = amountText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
