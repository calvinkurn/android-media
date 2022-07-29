package com.tokopedia.shop.flashsale.data.response

import com.google.gson.annotations.SerializedName

data class DoSellerCampaignCancellationResponse(
    @SerializedName("doSellerCampaignCancellation")
    val doSellerCampaignCancellation: DoSellerCampaignCancellation
) {
    data class ResponseHeader (
        @SerializedName("errorMessage")
        val errorMessage: List<String>
    )

    data class DoSellerCampaignCancellation (
        @SerializedName("is_success")
        val isSuccess: Boolean,
        @SerializedName("response_header")
        val responseHeader: ResponseHeader
    )
}