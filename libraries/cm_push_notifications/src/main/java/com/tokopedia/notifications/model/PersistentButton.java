package com.tokopedia.notifications.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.notifications.common.CMConstant;

/**
 * @author lalit.singh
 */
public class PersistentButton {

    @SerializedName(CMConstant.PayloadKeys.APP_LINK)
    String appLink;

    @SerializedName(CMConstant.PayloadKeys.TEXT)
    String text;

    @SerializedName(CMConstant.PayloadKeys.ICON)
    String icon;


    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
