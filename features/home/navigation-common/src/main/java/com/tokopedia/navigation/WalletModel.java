package com.tokopedia.navigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class WalletModel {
    @SerializedName("rawBalance")
    @Expose
    private Double rawBalance;
    @SerializedName("balance")
    @Expose
    private String balance;

    public Double getRawBalance() {
        return rawBalance;
    }

    public void setRawBalance(Double rawBalance) {
        this.rawBalance = rawBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
