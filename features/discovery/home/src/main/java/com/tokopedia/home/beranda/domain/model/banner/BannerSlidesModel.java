package com.tokopedia.home.beranda.domain.model.banner;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/28/17.
 */
public class BannerSlidesModel {
    public static String TYPE_BANNER_PERSO = "overlay";
    public static String TYPE_BANNER_DEFAULT = "default";

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("redirect_url")
    private String redirectUrl;
    @SerializedName("creative_name")
    private String creativeName;
    @SerializedName("applink")
    private String applink;
    @SerializedName("target")
    private int target;
    @SerializedName("device")
    private int device;
    @SerializedName("expire_time")
    private String expireTime;
    @SerializedName("state")
    private int state;
    @SerializedName("created_by")
    private int createdBy;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("updated_by")
    private int updatedBy;
    @SerializedName("updated_on")
    private String updatedOn;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("slide_index")
    private int slideIndex;
    @SerializedName("promo_code")
    private String promoCode;
    @SerializedName("topads_view_url")
    private String topadsViewUrl;
    @SerializedName("type")
    private String type;

    private int position;
    private boolean impressed;

    public String getTopadsViewUrl() {
        return topadsViewUrl;
    }

    public void setTopadsViewUrl(String topadsViewUrl) {
        this.topadsViewUrl = topadsViewUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCreativeName() {
        return creativeName;
    }

    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getSlideIndex() {
        return slideIndex;
    }

    public void setSlideIndex(int slideIndex) {
        this.slideIndex = slideIndex;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public void setImpressed(boolean impressed) {
        this.impressed = impressed;
    }

    public boolean isImpressed() {
        return impressed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
