package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/11/18.
 */
public class FeedModel {

    @SerializedName("newFeeds")
    @Expose
    private Boolean newFeeds;

    public Boolean getNewFeeds() {
        return newFeeds;
    }

    public void setNewFeeds(Boolean newFeeds) {
        this.newFeeds = newFeeds;
    }
}
