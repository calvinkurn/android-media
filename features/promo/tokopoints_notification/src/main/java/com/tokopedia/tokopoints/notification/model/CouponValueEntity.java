package com.tokopedia.tokopoints.notification.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class CouponValueEntity {
    @SerializedName(value = "imageUrlMobile", alternate = {"image_url_mobile", "imageURLMobile"})
    private String imageUrlMobile;

    @SerializedName(value = "subTitle", alternate = {"sub_title", "subtitle"})
    private String subTitle;

    @SerializedName("title")
    private String title;

    @SerializedName("expired")
    private String expired;

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.title)
                || TextUtils.isEmpty(this.imageUrlMobile);
    }

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "CouponValueEntity{" +
                "imageUrlMobile='" + imageUrlMobile + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
