package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponExtraInfoEntity {
    @Expose
    @SerializedName("info_html")
    private String infoHtml;

    @Expose
    @SerializedName("link_text")
    private String linkText;

    @Expose
    @SerializedName("link_url")
    private String linkUrl;

    public String getInfoHtml() {
        return infoHtml;
    }

    public void setInfoHtml(String infoHtml) {
        this.infoHtml = infoHtml;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "CouponExtraInfo{" +
                "infoHtml='" + infoHtml + '\'' +
                ", linkText='" + linkText + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
