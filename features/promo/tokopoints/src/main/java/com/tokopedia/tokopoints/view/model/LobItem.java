package com.tokopedia.tokopoints.view.model;

public class LobItem {
    private String appLink;
    private String icon;
    private String url;
    private String text;

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "LobItem{" +
                "appLink='" + appLink + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
