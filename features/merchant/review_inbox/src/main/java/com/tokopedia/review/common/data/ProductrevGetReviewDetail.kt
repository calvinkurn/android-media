package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetail (
        @SerializedName("product")
        @Expose
        val product: ProductrevGetReviewDetailProduct = ProductrevGetReviewDetailProduct(),
        @SerializedName("review")
        @Expose
        val review: ProductrevGetReviewDetailReview = ProductrevGetReviewDetailReview(),
        @SerializedName("response")
        @Expose
        val response: ProductrevGetReviewDetailResponse = ProductrevGetReviewDetailResponse(),
        @SerializedName("reputation")
        @Expose
        val reputation: ProductrevGetReviewDetailReputation = ProductrevGetReviewDetailReputation()
)