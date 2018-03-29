package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenUserEntity {

    @SerializedName("tokenUserID")
    @Expose
    private Integer tokenUserID;

    @SerializedName("campaignID")
    @Expose
    private Integer campaignID;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("unixTimestampFetch")
    @Expose
    private Integer unixTimestampFetch;

    @SerializedName("timeRemainingSeconds")
    @Expose
    private Integer timeRemainingSeconds;

    @SerializedName("isShowTime")
    @Expose
    private Boolean isShowTime;

    @SerializedName("tokenClass")
    @Expose
    private String tokenClass;

    @SerializedName("tokenAsset")
    @Expose
    private TokenAssetEntity tokenAsset;

    @SerializedName("backgroundImgUrl")
    @Expose
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

    public TokenAssetEntity getTokenAsset() {
        return tokenAsset;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }
}
