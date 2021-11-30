package com.tokopedia.review.common.data

import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailResponseWrapper (
    @SerializedName("productrevGetReviewDetailV2")
    val productrevGetReviewDetail: ProductrevGetReviewDetail = ProductrevGetReviewDetail()
)