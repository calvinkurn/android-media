package com.tokopedia.review.feature.details.data

import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailResponseWrapper (
    @SerializedName("productrevGetReviewDetail")
    val productrevGetReviewDetail: ProductrevGetReviewDetail = ProductrevGetReviewDetail()
)