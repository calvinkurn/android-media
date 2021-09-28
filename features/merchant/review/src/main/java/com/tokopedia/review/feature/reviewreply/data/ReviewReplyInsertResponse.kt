package com.tokopedia.review.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyInsertResponse(
        @SerializedName("inboxReviewInsertReviewResponseV2")
        val inboxReviewInsertReviewResponse: InboxReviewInsertReviewResponse = InboxReviewInsertReviewResponse()
) {
    data class InboxReviewInsertReviewResponse(
            @SerializedName("isSuccesss")
            val isSuccesss: String = "0"
    )
}