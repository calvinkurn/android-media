package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRevEditReviewResponseWrapper(
        @SerializedName("productRevEditReview")
        @Expose
        val productrevSuccessIndicator: ProductRevSuccessIndicator? = null
)