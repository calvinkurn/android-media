
package com.tokopedia.kol.feature.post.data.pojo.shop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentListData {
    @SerializedName("feed_content_post")
    @Expose
    private FeedContentPost feedContentPost;

    public FeedContentPost getFeedContentPost() {
        return feedContentPost;
    }

    public void setFeedContentPost(FeedContentPost feedContentPost) {
        this.feedContentPost = feedContentPost;
    }
}
