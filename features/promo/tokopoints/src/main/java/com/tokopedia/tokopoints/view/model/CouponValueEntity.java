package com.tokopedia.tokopoints.view.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.library.baseadapter.BaseItem;
import com.tokopedia.tokopoints.view.model.section.CouponUpperLeftSection;

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
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("icon")
    private String icon;

    @Expose
    @SerializedName(value = "imageUrlMobile", alternate = {"image_url_mobile", "imageURLMobile"})
    private String imageUrlMobile;

    @Expose
    @SerializedName(value = "thumbnailUrlMobile", alternate = {"thumbnail_url_mobile"})
    private String thumbnailUrlMobile;

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

    @SerializedName("redirectAppLink")
    private String redirectAppLink;

    @SerializedName("upperLeftSection")
    private CouponUpperLeftSection upperLeftSection;

    public CouponUpperLeftSection getUpperLeftSection() {
        return upperLeftSection;
    }

    public void setUpperLeftSection(CouponUpperLeftSection upperLeftSection) {
        this.upperLeftSection = upperLeftSection;
    }

    public String getRedirectAppLink() {
        return redirectAppLink;
    }

    public void setRedirectAppLink(String redirectAppLink) {
        this.redirectAppLink = redirectAppLink;
    }

    public boolean isStacked() {
        return isStacked;
    }

    public void setStacked(boolean stacked) {
        isStacked = stacked;
    }

    public boolean isNewCoupon() {
        return isNewCoupon;
    }

    public void setNewCoupon(boolean newCoupon) {
        isNewCoupon = newCoupon;
    }

    public String getStackId() {
        return stackId;
    }

    public void setStackId(String stackId) {
        this.stackId = stackId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrlMobile() {
        return imageUrlMobile;
    }

    public void setImageUrlMobile(String imageUrlMobile) {
        this.imageUrlMobile = imageUrlMobile;
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

    public String getHowToUse() {
        return howToUse;
    }

    public void setHowToUse(String howToUse) {
        this.howToUse = howToUse;
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
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", imageUrlMobile='" + imageUrlMobile + '\'' +
                ", thumbnailUrlMobile='" + thumbnailUrlMobile + '\'' +
                ", title='" + title + '\'' +
                ", usage=" + usage +
                ", howToUse='" + howToUse + '\'' +
                ", minimumUsage='" + minimumUsage + '\'' +
                ", minimumUsageLabel='" + minimumUsageLabel + '\'' +
                ", realCode='" + realCode + '\'' +
                ", tnc='" + tnc + '\'' +
                ", swipe=" + swipe +
                '}';
    }
}
