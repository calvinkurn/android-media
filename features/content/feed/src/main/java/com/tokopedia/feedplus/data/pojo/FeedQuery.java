package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by milhamj on 07/06/18.
 */

public class FeedQuery {
    @SerializedName("feed")
    @Expose
    private Feeds feed;

    public Feeds getFeed() {
        return feed;
    }
}
