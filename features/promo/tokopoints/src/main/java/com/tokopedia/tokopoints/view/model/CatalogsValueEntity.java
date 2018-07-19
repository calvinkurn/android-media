package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CatalogsValueEntity implements Serializable {
    @Expose
    @SerializedName("base_code")
    private String baseCode;

    @Expose
    @SerializedName("expired")
    private String expired;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("image_url")
    private String imageUrl;

    @Expose
    @SerializedName("image_url_mobile")
    private String imageUrlMobile;

    @Expose
    @SerializedName("isGift")
    private int isGift;

    @Expose
    @SerializedName("points")
    private String points;

    @Expose
    @SerializedName("points_str")
    private String pointsStr;

    @Expose
    @SerializedName("promo_id")
    private int promoId;

    @Expose
    @SerializedName("quota")
    private Integer quota;

    @Expose
    @SerializedName("slug")
    private String slug;

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
    @SerializedName("isDisabled")
    private boolean isDisabled;

    @Expose
    @SerializedName("isDisabledButton")
    private boolean isDisabledButton;

    @Expose
    @SerializedName("upperTextDesc")
    private String upperTextDesc;

    @Expose
    @SerializedName("expiredLabel")
    private String expiredLabel;

    @Expose
    @SerializedName("disableErrorMessage")
    private String disableErrorMessage;

    @Expose
    @SerializedName("expiredStr")
    private String expiredStr;

    public String getExpiredStr() {
        return expiredStr;
    }

    public void setExpiredStr(String expiredStr) {
        this.expiredStr = expiredStr;
    }

    public String getExpiredLabel() {
        return expiredLabel;
    }

    public void setExpiredLabel(String expiredLabel) {
        this.expiredLabel = expiredLabel;
    }

    public String getDisableErrorMessage() {
        return disableErrorMessage;
    }

    public void setDisableErrorMessage(String disableErrorMessage) {
        this.disableErrorMessage = disableErrorMessage;
    }

    public boolean isDisabledButton() {
        return isDisabledButton;
    }

    public void setDisabledButton(boolean disabledButton) {
        isDisabledButton = disabledButton;
    }

    public String getUpperTextDesc() {
        return upperTextDesc;
    }

    public void setUpperTextDesc(String upperTextDesc) {
        this.upperTextDesc = upperTextDesc;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPointsStr() {
        return pointsStr;
    }

    public void setPointsStr(String pointsStr) {
        this.pointsStr = pointsStr;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    @Override
    public String
    toString() {
        return "CatalogsValueEntity{" +
                "baseCode='" + baseCode + '\'' +
                ", expired='" + expired + '\'' +
                ", id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageUrlMobile='" + imageUrlMobile + '\'' +
                ", isGift=" + isGift +
                ", points='" + points + '\'' +
                ", pointsStr='" + pointsStr + '\'' +
                ", promoId=" + promoId +
                ", quota=" + quota +
                ", slug='" + slug + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thumbnailUrlMobile='" + thumbnailUrlMobile + '\'' +
                ", title='" + title + '\'' +
                ", isDisabled=" + isDisabled +
                ", isDisabledButton=" + isDisabledButton +
                ", upperTextDesc='" + upperTextDesc + '\'' +
                ", expiredLabel='" + expiredLabel + '\'' +
                ", disableErrorMessage='" + disableErrorMessage + '\'' +
                ", expiredStr='" + expiredStr + '\'' +
                '}';
    }
}
