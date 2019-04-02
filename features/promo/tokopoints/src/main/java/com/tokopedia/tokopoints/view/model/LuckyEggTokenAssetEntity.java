package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LuckyEggTokenAssetEntity {
    @Expose
    @SerializedName("floatingImgUrl")
    private String floatingImgUrl;

    public String getFloatingImgUrl() {
        return floatingImgUrl;
    }

    public void setFloatingImgUrl(String floatingImgUrl) {
        this.floatingImgUrl = floatingImgUrl;
    }

    @Override
    public String toString() {
        return "LuckyEggTokenAssetEntity{" +
                "floatingImgUrl='" + floatingImgUrl + '\'' +
                '}';
    }
}
