package com.tokopedia.recentview.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Badge {
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("img_url")
    @Expose
    String imgUrl;

    //sometimes it's different at ws (sometimes image_url and sometimes imgurl)
    @SerializedName("image_url")
    @Expose
    String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
