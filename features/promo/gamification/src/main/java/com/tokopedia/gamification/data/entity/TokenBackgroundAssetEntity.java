package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 4/4/18.
 */

public class TokenBackgroundAssetEntity {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("backgroundImgUrl")
    @Expose
    private String backgroundImgUrl;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }
}
