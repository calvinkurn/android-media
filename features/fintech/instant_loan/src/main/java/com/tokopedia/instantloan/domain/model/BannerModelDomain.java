package com.tokopedia.instantloan.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lavekush on 21/03/18.
 */

public class BannerModelDomain {
    private String image;
    private String link;

    public BannerModelDomain(String image, String link) {
        this.image = image;
        this.link = link;
    }

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
}
