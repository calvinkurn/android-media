package com.tokopedia.notifications.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author lalit.singh
 */
public class Grid {

    @SerializedName("appLink")
    private String appLink;
    @SerializedName("media")
    private Media media;

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
