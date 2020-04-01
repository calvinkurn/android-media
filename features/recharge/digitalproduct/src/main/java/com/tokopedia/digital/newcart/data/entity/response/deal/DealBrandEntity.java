package com.tokopedia.digital.newcart.data.entity.response.deal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealBrandEntity {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("featured_thumbnail_image")
    @Expose
    private String featuredImageThum;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public String getFeaturedImageThum() {
        return featuredImageThum;
    }
}
