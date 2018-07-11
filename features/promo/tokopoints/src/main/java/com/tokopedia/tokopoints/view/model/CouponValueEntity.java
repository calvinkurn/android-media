package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponValueEntity {
    @Expose
    @SerializedName("catalog_id")
    private int catalogId;

    @Expose
    @SerializedName("catalog_title")
    private String catalogTitle;

    @Expose
    @SerializedName("catalog_sub_title")
    private String catalogSubTitle;

    @Expose
    @SerializedName("code")
    private String code;

    @Expose
    @SerializedName("cta")
    private String cta;

    @Expose
    @SerializedName("ctaDesktop")
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
    @SerializedName("image_url")
    private String imageUrl;

    @Expose
    @SerializedName("image_url_mobile")
    private String imageUrlMobile;

    @Expose
    @SerializedName("promo_id")
    private int promoId;

    @Expose
    @SerializedName("sub_title")
    private String subTitle;

    @Expose
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;

    @Expose
    @SerializedName("thumbnail_url_mobile")
    private String thumbnailUrlMobile;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("usage")
    private CouponUsesEntity usage;

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogTitle() {
        return catalogTitle;
    }

    public void setCatalogTitle(String catalogTitle) {
        this.catalogTitle = catalogTitle;
    }

    public String getCatalogSubTitle() {
        return catalogSubTitle;
    }

    public void setCatalogSubTitle(String catalogSubTitle) {
        this.catalogSubTitle = catalogSubTitle;
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

    @Override
    public String toString() {
        return "CouponValueEntity{" +
                "catalogId=" + catalogId +
                ", catalogTitle='" + catalogTitle + '\'' +
                ", catalogSubTitle='" + catalogSubTitle + '\'' +
                ", code='" + code + '\'' +
                ", cta='" + cta + '\'' +
                ", ctaDesktop='" + ctaDesktop + '\'' +
                ", description='" + description + '\'' +
                ", expired='" + expired + '\'' +
                ", icon='" + icon + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageUrlMobile='" + imageUrlMobile + '\'' +
                ", promoId=" + promoId +
                ", subTitle='" + subTitle + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thumbnailUrlMobile='" + thumbnailUrlMobile + '\'' +
                ", title='" + title + '\'' +
                ", usage=" + usage +
                '}';
    }
}
