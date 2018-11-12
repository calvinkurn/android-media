package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/11/18.
 */
public class FeedModel {
    @SerializedName("newFeeds")
    @Expose
    private boolean newFeeds;

    public boolean isNewFeeds() {
        return newFeeds;
    }

    public void setNewFeeds(boolean newFeeds) {
        this.newFeeds = newFeeds;
    }
}
