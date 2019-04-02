package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoSweepDetailEntity {
    @Expose
    @SerializedName("mutualfund_account_status")
    private int accountStatus;
    @Expose
    @SerializedName("mutualfund_balance")
    private double balance;
    @Expose
    @SerializedName("autosweep_status")
    private int autoSweepStatus;
    @Expose
    @SerializedName("amount_limit")
    private long amountLimit;
    @Expose
    @SerializedName("text")
    private DetailText text;
    @Expose
    @SerializedName("show_autosweep")
    private int showAutoSweep;
    @Expose
    @SerializedName("dashboard_link")
    private String dashboardLink;
    @Expose
    @SerializedName("result")
    private ResultEntity result;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    @SerializedName("mf_autosweep_info_link")
    private String mfInfoLink;

    public String getMfInfoLink() {
        return mfInfoLink;
    }

    public void setMfInfoLink(String mfInfoLink) {
        this.mfInfoLink = mfInfoLink;
    }

    public String getDashboardLink() {
        return dashboardLink;
    }

    public void setDashboardLink(String dashboardLink) {
        this.dashboardLink = dashboardLink;
    }

    public int getShowAutoSweep() {
        return showAutoSweep;
    }

    public void setShowAutoSweep(int showAutoSweep) {
        this.showAutoSweep = showAutoSweep;
    }

    public DetailText getText() {
        return text;
    }

    public void setText(DetailText text) {
        this.text = text;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAutoSweepStatus() {
        return autoSweepStatus;
    }

    public void setAutoSweepStatus(int autoSweepStatus) {
        this.autoSweepStatus = autoSweepStatus;
    }

    public long getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(long amountLimit) {
        this.amountLimit = amountLimit;
    }

    @Override
    public String toString() {
        return "AutoSweepDetailEntity{" +
                "accountStatus=" + accountStatus +
                ", balance=" + balance +
                ", autoSweepStatus=" + autoSweepStatus +
                ", amountLimit=" + amountLimit +
                ", text=" + text +
                ", showAutoSweep=" + showAutoSweep +
                ", dashboardLink='" + dashboardLink + '\'' +
                ", result=" + result +
                ", mfInfoLink='" + mfInfoLink + '\'' +
                '}';
    }
}
