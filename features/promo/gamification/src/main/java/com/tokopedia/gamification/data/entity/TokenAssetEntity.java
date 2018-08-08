package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAssetEntity {

    @SerializedName("floatingImgUrl")
    @Expose
    private String floatingImgUrl;

    @SerializedName("smallImgUrl")
    @Expose
    private String smallImgUrl;

    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls;

    @SerializedName("spriteUrl")
    @Expose
    private String spriteUrl;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("version")
    @Expose
    private int version;

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getFloatingImgUrl() {
        return floatingImgUrl;
    }
}
