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


    @Override
    public String toString() {
        return "CouponExtraInfo{" +
                "infoHtml='" + infoHtml + '\'' +
                ", linkText='" + linkText + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
