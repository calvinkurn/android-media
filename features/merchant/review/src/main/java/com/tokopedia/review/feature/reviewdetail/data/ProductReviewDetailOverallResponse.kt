package com.tokopedia.review.feature.reviewdetail.data

import com.google.gson.annotations.SerializedName

data class ProductReviewDetailOverallResponse(
        @SerializedName("productrevGetReviewAggregateByProductV2")
        val productGetReviewAggregateByProduct: ProductGetReviewAggregateByProduct = ProductGetReviewAggregateByProduct()
) {
    data class ProductGetReviewAggregateByProduct(
            @SerializedName("period")
            val period: String? = "",
            @SerializedName("productName")
            val productName: String? = "",
            @SerializedName("ratingAverage")
            val ratingAverage: Float? = 0.0F,
            @SerializedName("ratingCount")
            val ratingCount: Int? = 0
    )
}