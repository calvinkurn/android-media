
package com.tokopedia.tokocash.qrpayment.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 11/15/17.
 */

public class ActionBalanceEntity {

    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("text")
    @Expose
    private String labelAction;
    @SerializedName("applinks")
    @Expose
    private String applinks;
    @SerializedName("visibility")
    @Expose
    private String visibility;

    public ActionBalanceEntity() {
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getLabelAction() {
        return labelAction;
    }

    public void setLabelAction(String labelAction) {
        this.labelAction = labelAction;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
