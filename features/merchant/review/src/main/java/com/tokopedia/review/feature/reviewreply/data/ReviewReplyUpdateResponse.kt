package com.tokopedia.review.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyUpdateResponse(
        @SerializedName("productrevUpdateSellerResponse")
        val productrevUpdateSellerResponse: ProductrevUpdateSellerResponse = ProductrevUpdateSellerResponse()
) {
    data class ProductrevUpdateSellerResponse(
            @SerializedName("data")
            val `data`: DataFeedback = DataFeedback(),
            @SerializedName("success")
            val success: Boolean = false
    ) {
        data class DataFeedback(
                @SerializedName("feedbackID")
                val feedbackID: Long? = 0,
                @SerializedName("responseBy")
                val responseBy: Int? = 0,
                @SerializedName("responseMessage")
                val responseMessage: String? = "",
                @SerializedName("shopID")
                val shopID: Long? = 0
        )
    }
}