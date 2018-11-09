package com.tokopedia.tkpd.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ashwani Tyagi on 09/11/18.
 */
public class VideoPushBannerModel {
    @SerializedName("layout")
    private String layout;

    @SerializedName("banner_name")
    private String bannerName;

    @SerializedName("link")
    private String link;

    @SerializedName("custom_banner_img")
    private String bannerImg;


    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }
}
