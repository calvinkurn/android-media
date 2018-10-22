package com.tokopedia.notifications.model;

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
public class ActionButton {
    private String text;
    private String applink;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
