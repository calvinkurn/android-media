package com.tokopedia.shop.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoSellerCampaignProductSubmissionResponse(
    @SerializedName("doSellerCampaignProductSubmission")
    val data: DoSellerCampaignProductSubmission
) {
    data class ResponseMessage (
        @SerializedName("error_message")
        val errorMessage: String
    )

    data class DoSellerCampaignProductSubmission (
        @SerializedName("is_success")
        val isSuccess: Boolean,
        @SerializedName("message")
        val message: ResponseMessage
    )
}