package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class WalletModel {
    @SerializedName("linked")
    @Expose
    private boolean linked;
    @SerializedName("rawBalance")
    @Expose
    private Double rawBalance;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("applinks")
    @Expose
    private String applink;
    @SerializedName("action")
    @Expose
    private WalletAction action;

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

    public class WalletAction {
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("redirect_url")
        @Expose
        private String redirectUrl;
        @SerializedName("applinks")
        @Expose
        private String applink;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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
    }
}
