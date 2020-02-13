package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wallet {
    @SerializedName("cash_balance")
    @Expose
    private String cash_balance;
    @SerializedName("raw_cash_balance")
    @Expose
    private Integer raw_cash_balance;
    @SerializedName("point_balance")
    @Expose
    private String point_balance;
    @SerializedName("errors")
    @Expose
    private List<WalletErrors> errors;

    public List<WalletErrors> getErrors() {
        return errors;
    }

    public String getCashBalance() {
        return cash_balance;
    }

    public Integer getRawCashBalance() {
        return raw_cash_balance;
    }

    public String getPointBalance() {
        return point_balance;
    }

}
