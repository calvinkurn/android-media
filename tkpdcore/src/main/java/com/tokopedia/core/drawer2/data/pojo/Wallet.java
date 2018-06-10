
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wallet {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("linked")
    @Expose
    private Boolean linked;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("rawBalance")
    @Expose
    private Integer rawBalance;
    @SerializedName("errors")
    @Expose
    private List<ErrorEntity> errors = null;
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
    private List<AbTag> abTags = null;
    @SerializedName("action")
    @Expose
    private Action action;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

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

    public List<ErrorEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorEntity> errors) {
        this.errors = errors;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Integer getRawTotalBalance() {
        return rawTotalBalance;
    }

    public void setRawTotalBalance(Integer rawTotalBalance) {
        this.rawTotalBalance = rawTotalBalance;
    }

    public String getHoldBalance() {
        return holdBalance;
    }

    public void setHoldBalance(String holdBalance) {
        this.holdBalance = holdBalance;
    }

    public Integer getRawHoldBalance() {
        return rawHoldBalance;
    }

    public void setRawHoldBalance(Integer rawHoldBalance) {
        this.rawHoldBalance = rawHoldBalance;
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

    public List<AbTag> getAbTags() {
        return abTags;
    }

    public void setAbTags(List<AbTag> abTags) {
        this.abTags = abTags;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}
