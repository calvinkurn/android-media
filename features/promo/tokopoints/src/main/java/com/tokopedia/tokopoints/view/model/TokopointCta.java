package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokopointCta {
    @Expose
    @SerializedName("icon")
    private String icon;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName("url")
    private String url;

    @Expose
    @SerializedName("applink")
    private String applink;

    @Expose
    @SerializedName("type")
    private String type;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TokopointCta{" +
                "icon='" + icon + '\'' +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", applink='" + applink + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
