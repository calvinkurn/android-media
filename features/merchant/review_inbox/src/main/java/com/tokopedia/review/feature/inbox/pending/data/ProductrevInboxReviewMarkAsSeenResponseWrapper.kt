package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevInboxReviewMarkAsSeenResponseWrapper(
        @SerializedName("productrevInboxReviewMarkAsSeen")
        @Expose
        val productrevInboxReviewMarkAsSeen: ProductrevInboxReviewMarkAsSeenResponse = ProductrevInboxReviewMarkAsSeenResponse()
)