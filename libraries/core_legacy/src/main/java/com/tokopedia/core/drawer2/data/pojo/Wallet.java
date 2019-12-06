
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Wallet {

    @SerializedName("linked")
    @Expose
    private Boolean linked;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("text")
    @Expose
    private String text;
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

    public List<AbTag> getAbTags() {
        return abTags;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
