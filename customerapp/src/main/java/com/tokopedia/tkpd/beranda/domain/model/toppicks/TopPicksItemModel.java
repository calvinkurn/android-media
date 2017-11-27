package com.tokopedia.tkpd.beranda.domain.model.toppicks;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class TopPicksItemModel {
    /**
     * id : 0
     * image_url : https://ecs7.tokopedia.net/img/cache/300-square/attachment/2017/9/27/7492183/7492183_1792374e-84ce-4846-8853-b071a0b62f65.jpg
     * image_landscape_url : https://ecs7.tokopedia.net/img/cache/315x165/attachment/2017/9/27/7492183/7492183_fff06894-abd8-4be8-880c-657d23f7404a.jpg
     * name : Multitasking Tanpa Kendala
     * url : https://www.tokopedia.com/catalog/53523/corsair-ddr4-vengeance-lpx-8-gb-2x4gb-cmk8gx4m2a2666c16?device=ios
     */

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
}
