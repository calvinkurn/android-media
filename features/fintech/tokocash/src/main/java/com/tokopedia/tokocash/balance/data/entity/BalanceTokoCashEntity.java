
package com.tokopedia.tokocash.balance.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class BalanceTokoCashEntity {

    @SerializedName("linked")
    @Expose
    private Boolean linked;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("rawBalance")
    @Expose
    private Integer rawBalance;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("total_balance")
    @Expose
    private String totalBalance;
    @SerializedName("raw_total_balance")
    @Expose
    private Integer rawTotalBalance;
    @SerializedName("hold_balance")
    @Expose
    private String holdBalance;
    @SerializedName("raw_hold_balance")
    @Expose
    private Integer rawHoldBalance;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("applinks")
    @Expose
    private String applinks;
    @SerializedName("ab_tags")
    @Expose
    private List<AbTagEntity> abTags = null;
    @SerializedName("action")
    @Expose
    private ActionEntity action;
    @SerializedName("point_balance")
    @Expose
    private String pointBalance;
    @SerializedName("raw_point_balance")
    @Expose
    private int rawPointBalance;
    @SerializedName("cash_balance")
    @Expose
    private String cashBalance;
    @SerializedName("raw_cash_balance")
    @Expose
    private int rawCashBalance;
    @SerializedName("wallet_type")
    @Expose
    private String walletType;
    @SerializedName("help_applink")
    @Expose
    private String helpApplink;
    @SerializedName("tnc_applink")
    @Expose
    private String tncApplink;
    @SerializedName("show_announcement")
    @Expose
    private boolean showAnnouncement;

    public Boolean getLinked() {
        return linked;
    }

    public void setLinked(Boolean linked) {
        this.linked = linked;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getRawBalance() {
        return rawBalance;
    }

    public void setRawBalance(Integer rawBalance) {
        this.rawBalance = rawBalance;
    }

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

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public List<AbTagEntity> getAbTags() {
        return abTags;
    }

    public ActionEntity getAction() {
        return action;
    }

    public void setAction(ActionEntity action) {
        this.action = action;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public Integer getRawTotalBalance() {
        return rawTotalBalance;
    }

    public String getHoldBalance() {
        return holdBalance;
    }

    public Integer getRawHoldBalance() {
        return rawHoldBalance;
    }

    public String getPointBalance() {
        return pointBalance;
    }

    public int getRawPointBalance() {
        return rawPointBalance;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public int getRawCashBalance() {
        return rawCashBalance;
    }

    public String getWalletType() {
        return walletType;
    }

    public String getHelpApplink() {
        return helpApplink;
    }

    public String getTncApplink() {
        return tncApplink;
    }

    public boolean isShowAnnouncement() {
        return showAnnouncement;
    }
}