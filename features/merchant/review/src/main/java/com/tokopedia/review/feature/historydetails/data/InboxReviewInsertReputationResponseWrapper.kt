package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewInsertReputationResponseWrapper(
        @SerializedName("inboxReviewInsertReputation")
        @Expose
        val inboxReviewInsertReputation: InboxReviewInsertReputation = InboxReviewInsertReputation()
)