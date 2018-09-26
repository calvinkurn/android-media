package com.tokopedia.train.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 27/12/17.
 */

public class BannerDetail {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("attributes")
    @Expose
    private BannerAttribute attributes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BannerAttribute getAttributes() {
        return attributes;
    }

    public void setAttributes(BannerAttribute attributes) {
        this.attributes = attributes;
    }

}
