package com.tokopedia.notifications.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.common.CMConstant;

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
public class ActionButton {
    @SerializedName(CMConstant.PayloadKeys.TEXT)
    private String text;

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    private String appLink;

    @SerializedName(CMConstant.PayloadKeys.ACTION_BUTTON_ICON)
    private String actionButtonIcon;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getActionButtonIcon() {
        return actionButtonIcon;
    }

    public void setActionButtonIcon(String actionButtonIcon) {
        this.actionButtonIcon = actionButtonIcon;
    }
}
