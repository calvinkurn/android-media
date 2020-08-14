package com.tokopedia.review.feature.inbox.container.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewReputationListResponseWrapper(
        @SerializedName("inboxReviewReputationList")
        @Expose
        val inboxReviewReputationList: InboxReviewReputationList = InboxReviewReputationList()
)