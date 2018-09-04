package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LobItem {
    @Expose
    @SerializedName("appLink")
    private String appLink;

    @Expose
    @SerializedName("icon")
    private String icon;

    @Expose
    @SerializedName("url")
    private String url;

    @Expose
    @SerializedName("text")
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