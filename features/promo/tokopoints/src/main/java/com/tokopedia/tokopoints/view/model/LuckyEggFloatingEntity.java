package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LuckyEggFloatingEntity {
    @Expose
    @SerializedName("applink")
    private String applink;

    @Expose
    @SerializedName("tokenClaimCustomText")
    private String tokenClaimCustomText;

    @Expose
    @SerializedName("tokenAsset")
    private LuckyEggTokenAssetEntity tokenAsset;

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getTokenClaimCustomText() {
        return tokenClaimCustomText;
    }

    public void setTokenClaimCustomText(String tokenClaimCustomText) {
        this.tokenClaimCustomText = tokenClaimCustomText;
    }

    public LuckyEggTokenAssetEntity getTokenAsset() {
        return tokenAsset;
    }

    public void setTokenAsset(LuckyEggTokenAssetEntity tokenAsset) {
        this.tokenAsset = tokenAsset;
    }
}
