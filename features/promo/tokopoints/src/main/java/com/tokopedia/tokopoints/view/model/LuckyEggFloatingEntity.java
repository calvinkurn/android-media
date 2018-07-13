package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LuckyEggFloatingEntity {
    @Expose
    @SerializedName("applink")
    private String applink;

    @Expose
    @SerializedName("isShowTime")
    private boolean isShowTime;

    @Expose
    @SerializedName("pageUrl")
    private String pageUrl;

    @Expose
    @SerializedName("timeRemainingSeconds")
    private String timeRemainingSeconds;

    @Expose
    @SerializedName("tokenClaimCustomText")
    private String tokenClaimCustomText;

    @Expose
    @SerializedName("tokenClaimText")
    private String tokenClaimText;

    @Expose
    @SerializedName("tokenId")
    private String tokenId;

    @Expose
    @SerializedName("unixTimestamp")
    private String unixTimestamp;

    @Expose
    @SerializedName("countingMessage")
    private List<String> countingMessage;

    @Expose
    @SerializedName("tokenAsset")
    private LuckyEggTokenAssetEntity tokenAsset;

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public boolean getIsShowTime() {
        return isShowTime;
    }

    public void setIsShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(String timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public String getTokenClaimCustomText() {
        return tokenClaimCustomText;
    }

    public void setTokenClaimCustomText(String tokenClaimCustomText) {
        this.tokenClaimCustomText = tokenClaimCustomText;
    }

    public String getTokenClaimText() {
        return tokenClaimText;
    }

    public void setTokenClaimText(String tokenClaimText) {
        this.tokenClaimText = tokenClaimText;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setUnixTimestamp(String unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }

    public List<String> getCountingMessage() {
        return countingMessage;
    }

    public void setCountingMessage(List<String> countingMessage) {
        this.countingMessage = countingMessage;
    }

    public LuckyEggTokenAssetEntity getTokenAsset() {
        return tokenAsset;
    }

    public void setTokenAsset(LuckyEggTokenAssetEntity tokenAsset) {
        this.tokenAsset = tokenAsset;
    }
}
