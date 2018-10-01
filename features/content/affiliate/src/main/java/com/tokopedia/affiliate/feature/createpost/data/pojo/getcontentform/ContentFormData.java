
package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentFormData {

    @SerializedName("feed_content_form")
    @Expose
    private FeedContentForm feedContentForm = new FeedContentForm();

    public FeedContentForm getFeedContentForm() {
        return feedContentForm;
    }

    public void setFeedContentForm(FeedContentForm feedContentForm) {
        this.feedContentForm = feedContentForm;
    }

}
