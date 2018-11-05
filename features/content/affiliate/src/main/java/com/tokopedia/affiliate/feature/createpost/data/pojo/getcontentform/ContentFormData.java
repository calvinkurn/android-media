
package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.affiliate.common.data.pojo.AffiliateCheckPojo;

public class ContentFormData {

    @SerializedName("feed_content_form")
    @Expose
    private FeedContentForm feedContentForm = new FeedContentForm();

    @SerializedName("affiliateCheck")
    @Expose
    private AffiliateCheckPojo affiliateCheck;

    public FeedContentForm getFeedContentForm() {
        return feedContentForm;
    }

    public void setFeedContentForm(FeedContentForm feedContentForm) {
        this.feedContentForm = feedContentForm;
    }

    public AffiliateCheckPojo getAffiliateCheck() {
        return affiliateCheck;
    }

    public void setAffiliateCheck(AffiliateCheckPojo affiliateCheck) {
        this.affiliateCheck = affiliateCheck;
    }
}
