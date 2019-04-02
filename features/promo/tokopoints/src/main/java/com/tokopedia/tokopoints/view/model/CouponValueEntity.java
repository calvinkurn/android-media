package com.tokopedia.tokopoints.view.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.library.baseadapter.BaseItem;

public class CouponValueEntity extends BaseItem {
    @Expose
    @SerializedName(value = "catalog_id", alternate = {"id"})
    private int catalogId;

    @Expose
    @SerializedName("code")
    private String code;

    @Expose
    @SerializedName("cta")
    private String cta;

    @Expose
    @SerializedName(value = "ctaDesktop", alternate = {"cta_desktop"})
    private String ctaDesktop;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("expired")
    private String expired;

    @Expose
    @SerializedName("icon")
    private String icon;

    @Expose
    @SerializedName(value = "imageUrl", alternate = {"image_url"})
    private String imageUrl;

    @Expose
    @SerializedName(value = "imageUrlMobile", alternate = {"image_url_mobile", "imageURLMobile"})
    private String imageUrlMobile;

    @Expose
    @SerializedName(value = "thumbnailUrl", alternate = {"thumbnail_url"})
    private String thumbnailUrl;

    @Expose
    @SerializedName(value = "thumbnailUrlMobile", alternate = {"thumbnail_url_mobile"})
    private String thumbnailUrlMobile;

    @Expose
    @SerializedName("promoID")
    private int promoId;

    @Expose
    @SerializedName(value = "subTitle", alternate = {"sub_title", "subtitle"})
    private String subTitle;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("usage")
    private CouponUsesEntity usage;

    @SerializedName(value = "howToUse", alternate = {"how_to_use"})
    private String howToUse;

    @SerializedName(value = "minimum_usage", alternate = {"minimumUsage"})
    private String minimumUsage;

    @SerializedName(value = "minimum_usage_label", alternate = {"minimumUsageLabel"})
    private String minimumUsageLabel;

    @SerializedName("overview")
    private String overview;

    @SerializedName("real_code")
    private String realCode;

    @SerializedName("tnc")
    private String tnc;

    @SerializedName("swipe")
    private CouponSwipeDetail swipe;

    @SerializedName("isStacked")
    private boolean isStacked;

    @SerializedName("isNewCoupon")
    private boolean isNewCoupon;

    @SerializedName("stackID")
    private String stackId;

    public CouponSwipeDetail getSwipe() {
        return swipe;
    }

    public void setSwipe(CouponSwipeDetail swipe) {
        this.swipe = swipe;
    }

    public String getMinimumUsage() {
        return minimumUsage;
    }

    public void setMinimumUsage(String minimumUsage) {
        this.minimumUsage = minimumUsage;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getCtaDesktop() {
        return ctaDesktop;
    }

    public void setCtaDesktop(String ctaDesktop) {
        this.ctaDesktop = ctaDesktop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrlMobile() {
        return thumbnailUrlMobile;
    }

    public void setThumbnailUrlMobile(String thumbnailUrlMobile) {
        this.thumbnailUrlMobile = thumbnailUrlMobile;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
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

    public CouponUsesEntity getUsage() {
        return usage;
    }

    public void setUsage(CouponUsesEntity usage) {
        this.usage = usage;
    }

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRealCode() {
        return realCode;
    }

    public void setRealCode(String realCode) {
        this.realCode = realCode;
    }

    public String getMinimumUsageLabel() {
        return minimumUsageLabel;
    }

    public void setMinimumUsageLabel(String minimumUsageLabel) {
        this.minimumUsageLabel = minimumUsageLabel;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(this.title)
                || TextUtils.isEmpty(this.imageUrlMobile);
    }

    @Override
    public String toString() {
        return "CouponValueEntity{" +
                "catalogId=" + catalogId +
                ", code='" + code + '\'' +
                ", cta='" + cta + '\'' +
                ", ctaDesktop='" + ctaDesktop + '\'' +
                ", description='" + description + '\'' +
                ", expired='" + expired + '\'' +
                ", icon='" + icon + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageUrlMobile='" + imageUrlMobile + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thumbnailUrlMobile='" + thumbnailUrlMobile + '\'' +
                ", promoId=" + promoId +
                ", subTitle='" + subTitle + '\'' +
                ", title='" + title + '\'' +
                ", usage=" + usage +
                ", howToUse='" + howToUse + '\'' +
                ", minimumUsage='" + minimumUsage + '\'' +
                ", minimumUsageLabel='" + minimumUsageLabel + '\'' +
                ", overview='" + overview + '\'' +
                ", realCode='" + realCode + '\'' +
                ", tnc='" + tnc + '\'' +
                ", swipe=" + swipe +
                '}';
    }
}
