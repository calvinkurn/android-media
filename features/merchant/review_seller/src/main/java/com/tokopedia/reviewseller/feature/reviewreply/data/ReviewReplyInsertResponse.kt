package com.tokopedia.reviewseller.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyInsertResponse(
        @SerializedName("inboxReviewInsertReviewResponse")
        val inboxReviewInsertReviewResponse: InboxReviewInsertReviewResponse = InboxReviewInsertReviewResponse()
) {
    data class InboxReviewInsertReviewResponse(
            @SerializedName("isSuccesss")
            val isSuccesss: Int = -1
    )
}