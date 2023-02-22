package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetInboxDesktop(
    @SerializedName("productrevGetInboxDesktop") @Expose val reviewDomain: ReviewDomain = ReviewDomain()
)
