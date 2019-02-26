package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.SerializedName;

public class VccUserBalance {
    @SerializedName("vcc_id")
    private int vccId = 0;

    @SerializedName("vcc_number")
    private int vccNumber = 0;

    @SerializedName("available_balance")
    private int availableBalance = 0;

    @SerializedName("credit_limit")
    private int creditLimit = 0;

    @SerializedName("outstanding_balance")
    private int outstandingBalance = 0;

    @SerializedName("available_balance_text")
    private String availableBalanceText = "";

    @SerializedName("credit_limit_text")
    private String creditLimitText = "";

    @SerializedName("currency")
    private String currency = "";

    @SerializedName("lender_name")
    private String lenderName = "";

    @SerializedName("vcc_expire_at")
    private String vccExpireAt = "";

    public int getVccId() {
        return vccId;
    }

    public void setVccId(int vccId) {
        this.vccId = vccId;
    }

    public int getVccNumber() {
        return vccNumber;
    }

    public void setVccNumber(int vccNumber) {
        this.vccNumber = vccNumber;
    }

    public int getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(int availableBalance) {
        this.availableBalance = availableBalance;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(int outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getAvailableBalanceText() {
        return availableBalanceText;
    }

    public void setAvailableBalanceText(String availableBalanceText) {
        this.availableBalanceText = availableBalanceText;
    }

    public String getCreditLimitText() {
        return creditLimitText;
    }

    public void setCreditLimitText(String creditLimitText) {
        this.creditLimitText = creditLimitText;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getVccExpireAt() {
        return vccExpireAt;
    }

    public void setVccExpireAt(String vccExpireAt) {
        this.vccExpireAt = vccExpireAt;
    }

    @Override
    public String toString() {
        return "VccUserBalance{" +
                "vccId=" + vccId +
                ", vccNumber=" + vccNumber +
                ", availableBalance=" + availableBalance +
                ", creditLimit=" + creditLimit +
                ", outstandingBalance=" + outstandingBalance +
                ", availableBalanceText='" + availableBalanceText + '\'' +
                ", creditLimitText='" + creditLimitText + '\'' +
                ", currency='" + currency + '\'' +
                ", lenderName='" + lenderName + '\'' +
                ", vccExpireAt='" + vccExpireAt + '\'' +
                '}';
    }
}
