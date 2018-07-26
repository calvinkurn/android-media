package com.tokopedia.topads.dashboard.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdsProductDetailDataSourceModel {

    @SerializedName("ad_id")
    @Expose
    private String adId;
    @SerializedName("ad_type")
    @Expose
    private String adType;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("price_bid")
    @Expose
    private float priceBid;
    @SerializedName("ad_budget")
    @Expose
    private String adBudget;
    @SerializedName("price_daily")
    @Expose
    private float priceDaily;
    @SerializedName("sticker_id")
    @Expose
    private String stickerId;
    @SerializedName("ad_schedule")
    @Expose
    private String adSchedule;
    @SerializedName("ad_start_date")
    @Expose
    private String adStartDate;
    @SerializedName("ad_start_time")
    @Expose
    private String adStartTime;
    @SerializedName("ad_end_date")
    @Expose
    private String adEndDate;
    @SerializedName("ad_end_time")
    @Expose
    private String adEndTime;
    @SerializedName("ad_image")
    @Expose
    private String adImage;
    @SerializedName("ad_title")
    @Expose
    private String adTitle;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("toggle")
    @Expose
    private String toggle;
    @SerializedName("suggested_bid_value")
    @Expose
    private long suggestionBidValue;
    @SerializedName("is_suggestion_bid_button")
    @Expose
    private String suggestionBidButton;
    @SerializedName("is_enough_deposit")
    @Expose
    private boolean isEnoughDeposit;

    public boolean isEnoughDeposit() {
        return isEnoughDeposit;
    }

    public void setEnoughDeposit(boolean enoughDeposit) {
        isEnoughDeposit = enoughDeposit;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(float priceBid) {
        this.priceBid = priceBid;
    }

    public String getAdBudget() {
        return adBudget;
    }

    public void setAdBudget(String adBudget) {
        this.adBudget = adBudget;
    }

    public float getPriceDaily() {
        return priceDaily;
    }

    public void setPriceDaily(float priceDaily) {
        this.priceDaily = priceDaily;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getAdSchedule() {
        return adSchedule;
    }

    public void setAdSchedule(String adSchedule) {
        this.adSchedule = adSchedule;
    }

    public String getAdStartDate() {
        return adStartDate;
    }

    public void setAdStartDate(String adStartDate) {
        this.adStartDate = adStartDate;
    }

    public String getAdStartTime() {
        return adStartTime;
    }

    public void setAdStartTime(String adStartTime) {
        this.adStartTime = adStartTime;
    }

    public String getAdEndDate() {
        return adEndDate;
    }

    public void setAdEndDate(String adEndDate) {
        this.adEndDate = adEndDate;
    }

    public String getAdEndTime() {
        return adEndTime;
    }

    public void setAdEndTime(String adEndTime) {
        this.adEndTime = adEndTime;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }

    public long getSuggestionBidValue() {
        return suggestionBidValue;
    }

    public void setSuggestionBidValue(long suggestionBidValue) {
        this.suggestionBidValue = suggestionBidValue;
    }

    public String getSuggestionBidButton() {
        return suggestionBidButton;
    }

    public void setSuggestionBidButton(String suggestionBidButton) {
        this.suggestionBidButton = suggestionBidButton;
    }
}