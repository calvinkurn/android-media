package com.tokopedia.gamification.floatingtoken.model;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenFloating {

    private String tokenClass;
    private Integer tokenId;
    private TokenAsset tokenAsset;
    private String pageUrl;
    private String applink;
    private Integer timeRemainingSeconds;
    private Boolean isShowTime;
    private Integer unixTimestamp;

    public String getTokenClass() {
        return tokenClass;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public TokenAsset getTokenAsset() {
        return tokenAsset;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getApplink() {
        return applink;
    }

    public Integer getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public Boolean getShowTime() {
        return isShowTime;
    }

    public Integer getUnixTimestamp() {
        return unixTimestamp;
    }

    public void setTokenClass(String tokenClass) {
        this.tokenClass = tokenClass;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    public void setTokenAsset(TokenAsset tokenAsset) {
        this.tokenAsset = tokenAsset;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public void setTimeRemainingSeconds(Integer timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }

    public void setShowTime(Boolean showTime) {
        isShowTime = showTime;
    }

    public void setUnixTimestamp(Integer unixTimestamp) {
        this.unixTimestamp = unixTimestamp;
    }
}
