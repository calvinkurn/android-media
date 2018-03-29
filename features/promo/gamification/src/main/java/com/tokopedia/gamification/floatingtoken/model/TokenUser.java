package com.tokopedia.gamification.floatingtoken.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenUser {

    private Integer tokenUserID;
    private Integer campaignID;
    private String title;
    private Integer unixTimestampFetch;
    private Integer timeRemainingSeconds;
    private Boolean isShowTime;
    private String tokenClass;
    private TokenAsset tokenAsset;
    private String backgroundImgUrl;

    public Integer getTokenUserID() {
        return tokenUserID;
    }

    public Integer getCampaignID() {
        return campaignID;
    }

    public String getTitle() {
        return title;
    }

    public Integer getUnixTimestampFetch() {
        return unixTimestampFetch;
    }

    public Integer getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public Boolean getShowTime() {
        return isShowTime;
    }

    public String getTokenClass() {
        return tokenClass;
    }

    public TokenAsset getTokenAsset() {
        return tokenAsset;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setTokenUserID(Integer tokenUserID) {
        this.tokenUserID = tokenUserID;
    }

    public void setCampaignID(Integer campaignID) {
        this.campaignID = campaignID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnixTimestampFetch(Integer unixTimestampFetch) {
        this.unixTimestampFetch = unixTimestampFetch;
    }

    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public void setShowTime(Boolean showTime) {
        isShowTime = showTime;
    }

    public void setTokenClass(String tokenClass) {
        this.tokenClass = tokenClass;
    }

    public void setTokenAsset(TokenAsset tokenAsset) {
        this.tokenAsset = tokenAsset;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }
}
