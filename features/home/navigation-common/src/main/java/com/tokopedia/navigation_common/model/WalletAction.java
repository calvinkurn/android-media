package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 04/09/18.
 */
public class WalletAction {

    @SerializedName("text")
    @Expose
    private String text = "";
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl = "";
    @SerializedName("applinks")
    @Expose
    private String applink = "";

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
