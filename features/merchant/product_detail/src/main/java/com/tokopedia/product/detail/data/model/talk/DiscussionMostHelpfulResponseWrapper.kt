package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionMostHelpfulResponseWrapper(
        @SerializedName("discussionMostHelpful")
        @Expose
        val discussionMostHelpful: DiscussionMostHelpful = DiscussionMostHelpful()
)