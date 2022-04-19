package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewInsertReputation(
        @SerializedName("success")
        @Expose
        val success: Int = 0
)