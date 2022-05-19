package com.tokopedia.review.feature.reviewreply.update.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewReplyUpdateResponse(
    @SerializedName("productrevUpdateSellerResponseV2")
    @Expose
    val productrevUpdateSellerResponse: ProductrevUpdateSellerResponse = ProductrevUpdateSellerResponse()
) {
    data class ProductrevUpdateSellerResponse(
        @SerializedName("data")
        @Expose
        val `data`: DataFeedback = DataFeedback(),
        @SerializedName("success")
        @Expose
        val success: Boolean = false
    ) {
        data class DataFeedback(
            @SerializedName("responseMessage")
            @Expose
            val responseMessage: String? = "",
        )
    }
}