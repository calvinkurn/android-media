
package com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitPostData {

    @SerializedName("feed_content_submit")
    @Expose
    private FeedContentSubmit feedContentSubmit;

    public FeedContentSubmit getFeedContentSubmit() {
        return feedContentSubmit;
    }

    public void setFeedContentSubmit(FeedContentSubmit feedContentSubmit) {
        this.feedContentSubmit = feedContentSubmit;
    }

}
