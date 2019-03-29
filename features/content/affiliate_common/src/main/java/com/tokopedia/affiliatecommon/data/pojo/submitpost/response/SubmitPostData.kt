package com.tokopedia.affiliatecommon.data.pojo.submitpost.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitPostData (
    @SerializedName("feed_content_submit")
    @Expose
    var feedContentSubmit: FeedContentSubmit = FeedContentSubmit()
) {
    companion object {
        const val SUCCESS = 1
    }
}
