package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksModel {

    @SerializedName("name")
    private String name;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_landscape_url")
    private String imageLandscapeUrl;
    @SerializedName("url")
    private String url;
    @SerializedName("item")
    private List<TopPicksItemModel> item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLandscapeUrl() {
        return imageLandscapeUrl;
    }

    public void setImageLandscapeUrl(String imageLandscapeUrl) {
        this.imageLandscapeUrl = imageLandscapeUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TopPicksItemModel> getItem() {
        return item;
    }

    public void setItem(List<TopPicksItemModel> item) {
        this.item = item;
    }

}
