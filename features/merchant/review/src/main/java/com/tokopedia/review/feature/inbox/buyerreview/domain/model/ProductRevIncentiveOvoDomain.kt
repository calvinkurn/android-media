package com.tokopedia.review.feature.inbox.buyerreview.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevIncentiveOvoDomain(
        @SerializedName("productrevIncentiveOvo")
        @Expose
        val productrevIncentiveOvo: ProductRevIncentiveOvoResponse? = ProductRevIncentiveOvoResponse()
)