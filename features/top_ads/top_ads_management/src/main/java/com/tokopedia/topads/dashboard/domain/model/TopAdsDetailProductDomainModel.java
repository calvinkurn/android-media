package com.tokopedia.topads.dashboard.domain.model;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdsDetailProductDomainModel {

    private String adId;
    private String adType;
    private String groupId;
    private String shopId;
    private String itemId;
    private String status;
    private float priceBid;
    private String adBudget;
    private float priceDaily;
    private String stickerId;
    private String adSchedule;
    private String adStartDate;
    private String adStartTime;
    private String adEndDate;
    private String adEndTime;
    private String adImage;
    private String adTitle;
    private long suggestionBidValue;
    private String suggestionBidButton;
    private boolean isEnoughDeposit;

    public boolean isEnoughDeposit() {
        return isEnoughDeposit;
    }

    public void setEnoughDeposit(boolean enoughDeposit) {
        isEnoughDeposit = enoughDeposit;
    }

    public String getAdEndDate() {
        return adEndDate;
    }

    public void setAdEndDate(String adEndDate) {
        this.adEndDate = adEndDate;
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
