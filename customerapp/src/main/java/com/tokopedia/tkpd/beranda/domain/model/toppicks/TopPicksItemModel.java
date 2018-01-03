package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksItemModel {

    @SerializedName("id")
    private int id;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_landscape_url")
    private String imageLandscapeUrl;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("applinks")
    @Expose
    private String applinks="";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }
}
