package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAssetEntity {

    @SerializedName("smallImgUrl")
    @Expose
    private String smallImgUrl;

    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls;

    @SerializedName("spriteUrl")
    @Expose
    private String spriteUrl;

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }
}
