package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenFloatingEntity {

    @SerializedName("tokenClass")
    @Expose
    private String tokenClass;

    @SerializedName("tokenId")
    @Expose
    private Integer tokenId;

    @SerializedName("tokenAsset")
    @Expose
    private TokenAssetEntity tokenAsset;

    @SerializedName("pageUrl")
    @Expose
    private String pageUrl;

    @SerializedName("applink")
    @Expose
    private String applink;

    @SerializedName("timeRemainingSeconds")
    @Expose
    private Integer timeRemainingSeconds;

    @SerializedName("isShowTime")
    @Expose
    private Boolean isShowTime;

    @SerializedName("unixTimestamp")
    @Expose
    private Integer unixTimestamp;

    public String getTokenClass() {
        return tokenClass;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public TokenAssetEntity getTokenAsset() {
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
}
