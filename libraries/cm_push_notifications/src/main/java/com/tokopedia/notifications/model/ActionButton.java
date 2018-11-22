package com.tokopedia.notifications.model;

/**
 * Created by Ashwani Tyagi on 22/10/18.
 */
public class ActionButton {
    private String text;
    private String appLink;

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
}
