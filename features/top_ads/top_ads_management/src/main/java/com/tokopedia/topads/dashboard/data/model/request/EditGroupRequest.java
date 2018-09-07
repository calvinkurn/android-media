package com.tokopedia.topads.dashboard.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class EditGroupRequest {
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("toggle")
    @Expose
    private String toggle;
    @SerializedName("price_bid")
    @Expose
    private Integer priceBid;
    @SerializedName("price_daily")
    @Expose
    private Integer priceDaily;
    @SerializedName("group_budget")
    @Expose
    private String groupBudget;
    @SerializedName("group_schedule")
    @Expose
    private String groupSchedule;
    @SerializedName("group_start_date")
    @Expose
    private String groupStartDate;
    @SerializedName("group_start_time")
    @Expose
    private String groupStartTime;
    @SerializedName("group_end_date")
    @Expose
    private String groupEndDate;
    @SerializedName("group_end_time")
    @Expose
    private String groupEndTime;
    @SerializedName("sticker_id")
    @Expose
    private String stickerId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("suggested_bid_value")
    @Expose
    private long suggestionBidValue;
    @SerializedName("is_suggestion_bid_button")
    @Expose
    private String suggestionBidButton;

    public EditGroupRequest(String groupId, String groupName, String shopId, String toggle, Integer priceBid, Integer priceDaily, String groupBudget,
                            String groupSchedule, String groupStartDate, String groupStartTime, String groupEndDate, String groupEndTime,
                            String stickerId, String source, long suggestionBidValue, String suggestionBidButton) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.shopId = shopId;
        this.toggle = toggle;
        this.priceBid = priceBid;
        this.priceDaily = priceDaily;
        this.groupBudget = groupBudget;
        this.groupSchedule = groupSchedule;
        this.groupStartDate = groupStartDate;
        this.groupStartTime = groupStartTime;
        this.groupEndDate = groupEndDate;
        this.groupEndTime = groupEndTime;
        this.stickerId = stickerId;
        this.source = source;
        this.suggestionBidValue = suggestionBidValue;
        this.suggestionBidButton = suggestionBidButton;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getToggle() {
        return toggle;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
    }

    public Integer getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(Integer priceBid) {
        this.priceBid = priceBid;
    }

    public Integer getPriceDaily() {
        return priceDaily;
    }

    public void setPriceDaily(Integer priceDaily) {
        this.priceDaily = priceDaily;
    }

    public String getGroupBudget() {
        return groupBudget;
    }

    public void setGroupBudget(String groupBudget) {
        this.groupBudget = groupBudget;
    }

    public String getGroupSchedule() {
        return groupSchedule;
    }

    public void setGroupSchedule(String groupSchedule) {
        this.groupSchedule = groupSchedule;
    }

    public String getGroupStartDate() {
        return groupStartDate;
    }

    public void setGroupStartDate(String groupStartDate) {
        this.groupStartDate = groupStartDate;
    }

    public String getGroupStartTime() {
        return groupStartTime;
    }

    public void setGroupStartTime(String groupStartTime) {
        this.groupStartTime = groupStartTime;
    }

    public String getGroupEndDate() {
        return groupEndDate;
    }

    public void setGroupEndDate(String groupEndDate) {
        this.groupEndDate = groupEndDate;
    }

    public String getGroupEndTime() {
        return groupEndTime;
    }

    public void setGroupEndTime(String groupEndTime) {
        this.groupEndTime = groupEndTime;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
