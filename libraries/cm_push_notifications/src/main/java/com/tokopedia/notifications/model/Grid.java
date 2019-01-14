package com.tokopedia.notifications.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class Grid {

    @SerializedName("appLink")
    private String appLink;
    @SerializedName("img")
    private String img;

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
