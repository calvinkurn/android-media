package com.tokopedia.shop.flash_sale.data.response

import com.google.gson.annotations.SerializedName

data class GetMerchantCampaignTNCResponse(
    @SerializedName("getMerchantCampaignTNC")
    val getMerchantCampaignTNC: GetMerchantCampaignTNC = GetMerchantCampaignTNC(),
) {
    data class GetMerchantCampaignTNC(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("messages")
        val messages: List<String> = listOf(),
        @SerializedName("error")
        val error: Error = Error()
    )
    data class Error(
        @SerializedName("error_code")
        val error_code: Int = 0,
        @SerializedName("error_message")
        val error_message: String = "",
    )
}
