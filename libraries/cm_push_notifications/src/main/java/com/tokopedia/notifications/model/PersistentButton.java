package com.tokopedia.notifications.model;

/**
 * @author lalit.singh
 */
public class PersistentButton {

    String appLink;
    String text;
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
