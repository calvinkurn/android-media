package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform

import com.google.gson.annotations.SerializedName

data class FeedContentResponse(
        @SerializedName("feed_content_form")
        val feedContentForm: FeedContentForm = FeedContentForm()
)