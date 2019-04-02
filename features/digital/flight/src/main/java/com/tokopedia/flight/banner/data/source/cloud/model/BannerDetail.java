package com.tokopedia.flight.banner.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 27/12/17.
 */

public class BannerDetail {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("attributes")
    @Expose
    private BannerAttribute attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return "BannerDetail{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", attributes=" + attributes.toString() +
                '}';
    }
}
