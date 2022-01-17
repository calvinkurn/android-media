package com.tokopedia.review.feature.reviewreply.update.domain.model

import com.google.gson.annotations.SerializedName

data class ReviewReplyUpdateResponse(
    @SerializedName("productrevUpdateSellerResponseV2")
    val productrevUpdateSellerResponse: ProductrevUpdateSellerResponse = ProductrevUpdateSellerResponse()
) {
    data class ProductrevUpdateSellerResponse(
        @SerializedName("data")
        val `data`: DataFeedback = DataFeedback(),
        @SerializedName("success")
        val success: Boolean = false
    ) {
        data class DataFeedback(
            @SerializedName("responseMessage")
            val responseMessage: String? = "",
        )
    }
}