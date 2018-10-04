package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lavekush on 22/03/18.
 */

public class BannerEntity {
    @SerializedName("image")
    private String image;

    @SerializedName("link")
    private String link;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public BannerEntity(String image, String link) {
        this.image = image;
        this.link = link;
    }
}
