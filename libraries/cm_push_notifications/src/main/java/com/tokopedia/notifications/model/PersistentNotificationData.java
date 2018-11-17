package com.tokopedia.notifications.model;

/**
 * @author lalit.singh
 */
public class PersistentNotificationData {
    String appLink;
    String btnText;
    String btnImageUrl;


    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public String getBtnImageUrl() {
        return btnImageUrl;
    }

    public void setBtnImageUrl(String btnImageUrl) {
        this.btnImageUrl = btnImageUrl;
    }
}
