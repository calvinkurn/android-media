package com.tokopedia.tkpd.splash;

import com.google.gson.annotations.SerializedName;

public class DynamicBackground {

    @SerializedName("image_url")
    private String imageUrl;

    DynamicBackground(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
