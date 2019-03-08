package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallet {
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("rawBalance")
    @Expose
    private Integer rawBalance;
    @SerializedName("cash_balance")
    @Expose
    private String cash_balance;
    @SerializedName("raw_cash_balance")
    @Expose
    private Integer raw_cash_balance;
    @SerializedName("point_balance")
    @Expose
    private String point_balance;
    @SerializedName("raw_point_balance")
    @Expose
    private Integer raw_point_balance;

    public String getBalance() {
        return balance;
    }

    public Integer getRawBalance() {
        return rawBalance;
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

    public Integer getRawPointBalance() {
        return raw_point_balance;
    }
}
