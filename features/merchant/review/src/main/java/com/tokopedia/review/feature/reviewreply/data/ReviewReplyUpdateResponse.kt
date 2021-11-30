package com.tokopedia.review.feature.reviewreply.data

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
                @SerializedName("feedbackIDStr")
                val feedbackID: String? ="",
                @SerializedName("responseByStr")
                val responseBy: String? = "",
                @SerializedName("responseMessage")
                val responseMessage: String? = "",
                @SerializedName("shopIDStr")
                val shopID: String? = ""
        )
    }
}