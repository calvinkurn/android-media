package com.tokopedia.gamification.floating.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FloatingCtaEntity {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("appLink")
    @Expose
    private String appLink;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

}
