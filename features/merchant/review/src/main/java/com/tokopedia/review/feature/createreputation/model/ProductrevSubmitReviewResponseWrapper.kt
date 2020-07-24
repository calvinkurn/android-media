package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.feature.createreputation.model.ProductRevSubmitReview

data class ProductrevSubmitReviewResponseWrapper(
        @SerializedName("productrevSubmitReview")
        @Expose
        val productrevSubmitReview: ProductRevSubmitReview? = null
)