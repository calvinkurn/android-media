package com.tokopedia.digital_deals.domain.model.branddetailsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BrandDomain {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("seo_url")
    @Expose
    private String seoUrl;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("featured_thumbnail_image")
    @Expose
    private String featuredThumbnailImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getFeaturedThumbnailImage() {
        return featuredThumbnailImage;
    }

    public void setFeaturedThumbnailImage(String featuredThumbnailImage) {
        this.featuredThumbnailImage = featuredThumbnailImage;
    }



}