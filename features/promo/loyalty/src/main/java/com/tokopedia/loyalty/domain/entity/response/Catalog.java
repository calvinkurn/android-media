package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/12/17.
 */

public class Catalog {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("points")
    @Expose
    private int points;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;
    @SerializedName("thumbnail_url_mobile")
    @Expose
    private String thumbnailUrlMobile;

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getPoints() {
        return points;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getThumbnailUrlMobile() {
        return thumbnailUrlMobile;
    }
}
