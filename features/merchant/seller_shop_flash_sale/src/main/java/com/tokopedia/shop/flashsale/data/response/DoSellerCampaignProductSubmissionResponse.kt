package com.tokopedia.shop.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoSellerCampaignProductSubmissionResponse(
    @SerializedName("doSellerCampaignProductSubmission")
    val doSellerCampaignProductSubmission: DoSellerCampaignProductSubmission = DoSellerCampaignProductSubmission()
) {
    data class DoSellerCampaignProductSubmission(
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
        @SerializedName("message")
        val message: Message = Message(),
        @SerializedName("product_failed")
        val productFailed: List<ProductFailed> = listOf(),
        @SerializedName("product_success")
        val productSuccess: List<ProductSuccess> = listOf()
    ) {
        data class Message(
            @SerializedName("error_code")
            val errorCode: Int = 0,
            @SerializedName("error_message")
            val errorMessage: String = ""
        )

        data class ProductFailed(
            @SerializedName("message")
            val message: String = "",
            @SerializedName("product_id")
            val productId: String = ""
        )

        data class ProductSuccess(
            @SerializedName("message")
            val message: String = "",
            @SerializedName("product_id")
            val productId: String = ""
        )
    }
}