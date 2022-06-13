package com.tokopedia.review.feature.reviewreply.insert.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewReplyInsertResponse(
    @SerializedName("productrevInsertSellerResponse")
    @Expose
    val productrevInsertSellerResponse: ProductrevInsertSellerResponse = ProductrevInsertSellerResponse()
) {
    data class ProductrevInsertSellerResponse(
        @SerializedName("success")
        @Expose
        val success: Boolean? = false
    )
}