package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponButtonUsageEntity {
    @Expose
    @SerializedName(value = "appLink", alternate = {"applink"})
    private String appLink;

    @Expose
    @SerializedName("text")
    private String text;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("url")
    private String url;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CouponButtonUsageEntity{" +
                "appLink='" + appLink + '\'' +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
