package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class WalletModel {
    @SerializedName("text")
    @Expose
    private String text = "";
    @SerializedName("linked")
    @Expose
    private boolean linked = false;
    @SerializedName("rawBalance")
    @Expose
    private Long rawBalance = 0L;
    @SerializedName("balance")
    @Expose
    private String balance = "";
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl = "";
    @SerializedName("applinks")
    @Expose
    private String applink = "";
    @SerializedName("action")
    @Expose
    private WalletAction action = new WalletAction();
    @SerializedName("point_balance")
    @Expose
    private String pointBalance = "";
    @SerializedName("cash_balance")
    @Expose
    private String cashBalance = "";
    @SerializedName("wallet_type")
    @Expose
    private String walletType = "";

    private String pendingCashback = "";
    private int amountPendingCashback = 0;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public WalletAction getAction() {
        return action;
    }

    public void setAction(WalletAction action) {
        this.action = action;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public Long getRawBalance() {
        return rawBalance;
    }

    public void setRawBalance(Long rawBalance) {
        this.rawBalance = rawBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(String pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getPendingCashback() {
        return pendingCashback;
    }

    public void setPendingCashback(String pendingCashback) {
        this.pendingCashback = pendingCashback;
    }

    public int getAmountPendingCashback() {
        return amountPendingCashback;
    }

    public void setAmountPendingCashback(int amountPendingCashback) {
        this.amountPendingCashback = amountPendingCashback;
    }
}
