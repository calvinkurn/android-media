package com.tokopedia.train.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 27/12/17.
 */

public class BannerAttribute {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("linkURL")
    @Expose
    private String linkUrl;
    @SerializedName("imageURL")
    @Expose
    private String imageUrl;
    @SerializedName("promoCode")
    @Expose
    private String promoCode;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}
