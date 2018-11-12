package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/11/18.
 */
public class FeedModel {
    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("newFeeds")
    @Expose
    private Boolean newFeeds;

    public Boolean getNewFeeds() {
        return newFeeds;
    }

    public void setNewFeeds(Boolean newFeeds) {
        this.newFeeds = newFeeds;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
