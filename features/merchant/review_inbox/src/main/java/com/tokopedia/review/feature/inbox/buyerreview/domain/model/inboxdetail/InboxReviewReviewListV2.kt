package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReviewReviewListV2(
    @SerializedName("inboxReviewReviewListV2") @Expose val reviewDomain: ReviewDomain = ReviewDomain()
)
