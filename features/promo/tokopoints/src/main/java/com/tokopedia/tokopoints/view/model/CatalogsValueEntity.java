package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CatalogsValueEntity implements Serializable {
    @Expose
    @SerializedName("baseCode")
    private String baseCode;

    @Expose
    @SerializedName("expired")
    private String expired;

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName(value = "imageURL", alternate = {"image_url"})
    private String imageUrl;

    @Expose
    @SerializedName(value = "imageUrlMobile", alternate = {"image_url_mobile"})
    private String imageUrlMobile;

    @Expose
    @SerializedName(value = "isGift", alternate = {"is_gift"})
    private int isGift;

    @Expose
    @SerializedName("points")
    private String points;

    @Expose
    @SerializedName(value = "pointsStr", alternate = {"points_str"})
    private String pointsStr;

    @Expose
    @SerializedName("promoID")
    private int promoId;

    @Expose
    @SerializedName("quota")
    private int quota;

    @Expose
    @SerializedName("slug")
    private String slug;

    @Expose
    @SerializedName(value = "subtitle", alternate = {"sub_title"})
    private String subTitle;

    @Expose
    @SerializedName(value = "thumbnailURL", alternate = {"thumbnail_url"})
    private String thumbnailUrl;

    @Expose
    @SerializedName(value = "thumbnailURLMobile", alternate = {"thumbnail_url_mobile"})
    private String thumbnailUrlMobile;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName(value = "isDisabled", alternate = {"is_disabled"})
    private boolean isDisabled;

    @Expose
    @SerializedName(value = "isDisabledButton", alternate = {"is_disabled_button"})
    private boolean isDisabledButton;

    @Expose
    @SerializedName(value = "upperTextDesc", alternate = {"upper_text_desc"})
    private List<String> upperTextDesc;

    @Expose
    @SerializedName(value = "expiredLabel", alternate = {"expired_label"})
    private String expiredLabel;

    @Expose
    @SerializedName(value = "disableErrorMessage", alternate = {"disable_error_message"})
    private String disableErrorMessage;

    @Expose
    @SerializedName(value = "expiredStr", alternate = {"expired_str"})
    private String expiredStr;

    @Expose
    @SerializedName(value = "catalogType", alternate = {"catalog_type"})
    private int catalogType;

    @Expose
    @SerializedName(value = "pointsSlash", alternate = {"points_slash"})
    private int pointsSlash;

    @Expose
    @SerializedName(value = "pointsSlashStr", alternate = {"points_slash_str"})
    private String pointsSlashStr;

    @Expose
    @SerializedName(value = "discountPercentage", alternate = {"discount_percentage"})
    private int discountPercentage;

    @Expose
    @SerializedName(value = "discountPercentageStr", alternate = {"discount_percentage_str"})
    private String discountPercentageStr;

    @SerializedName(value = "buttonStr", alternate = {"button_str"})
    private String buttonStr;

    @Expose
    @SerializedName(value = "howToUse", alternate = {"how_to_use"})
    private String howToUse;

    @Expose
    @SerializedName("overview")
    private String overview;

    @Expose
    @SerializedName("tnc")
    private String tnc;

    @Expose
    @SerializedName("cta")
    private String cta;

    @Expose
    @SerializedName("isShowTukarButton")
    private boolean isShowTukarButton;

    @SerializedName("quotaPercentage")
    private Integer quotaPercentage;

    public Integer getQuotaPercentage() {
        return quotaPercentage;
    }

    public void setQuotaPercentage(Integer quotaPercentage) {
        this.quotaPercentage = quotaPercentage;
    }

    public boolean isShowTukarButton() {
        return isShowTukarButton;
    }

    public void setShowTukarButton(boolean showTukarButton) {
        isShowTukarButton = showTukarButton;
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

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
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

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isDisabledButton() {
        return isDisabledButton;
    }

    public void setDisabledButton(boolean disabledButton) {
        isDisabledButton = disabledButton;
    }

    public List<String> getUpperTextDesc() {
        return upperTextDesc;
    }

    public void setUpperTextDesc(List<String> upperTextDesc) {
        this.upperTextDesc = upperTextDesc;
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

    public String getExpiredStr() {
        return expiredStr;
    }

    public void setExpiredStr(String expiredStr) {
        this.expiredStr = expiredStr;
    }

    public int getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(int catalogType) {
        this.catalogType = catalogType;
    }

    public int getPointsSlash() {
        return pointsSlash;
    }

    public void setPointsSlash(int pointsSlash) {
        this.pointsSlash = pointsSlash;
    }

    public String getPointsSlashStr() {
        return pointsSlashStr;
    }

    public void setPointsSlashStr(String pointsSlashStr) {
        this.pointsSlashStr = pointsSlashStr;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountPercentageStr() {
        return discountPercentageStr;
    }

    public void setDiscountPercentageStr(String discountPercentageStr) {
        this.discountPercentageStr = discountPercentageStr;
    }

    public String getButtonStr() {
        return buttonStr;
    }

    public void setButtonStr(String buttonStr) {
        this.buttonStr = buttonStr;
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

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    @Override
    public String toString() {
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
                ", upperTextDesc=" + upperTextDesc +
                ", expiredLabel='" + expiredLabel + '\'' +
                ", disableErrorMessage='" + disableErrorMessage + '\'' +
                ", expiredStr='" + expiredStr + '\'' +
                ", catalogType=" + catalogType +
                ", pointsSlash=" + pointsSlash +
                ", pointsSlashStr='" + pointsSlashStr + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", discountPercentageStr='" + discountPercentageStr + '\'' +
                ", buttonStr='" + buttonStr + '\'' +
                ", howToUse='" + howToUse + '\'' +
                ", overview='" + overview + '\'' +
                ", tnc='" + tnc + '\'' +
                ", cta='" + cta + '\'' +
                ", isShowTukarButton=" + isShowTukarButton +
                ", quotaPercentage=" + quotaPercentage +
                '}';
    }
}
