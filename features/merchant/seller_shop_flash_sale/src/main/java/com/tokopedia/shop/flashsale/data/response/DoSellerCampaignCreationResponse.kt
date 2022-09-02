package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class DoSellerCampaignCreationResponse(
    @SerializedName("doSellerCampaignCreation")
    val doSellerCampaignCreation: DoSellerCampaignCreation = DoSellerCampaignCreation()
) {
    data class DoSellerCampaignCreation(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("campaign_id")
        val campaignId: String = "",
        @SerializedName("is_success")
        val isSuccess: Boolean = false,
        @SerializedName("product_failed")
        val productFailed: List<Any> = listOf(),
        @SerializedName("seller_campaign_error_creation")
        val sellerCampaignErrorCreation: SellerCampaignErrorCreation = SellerCampaignErrorCreation(),
        @SerializedName("total_product_failed")
        val totalProductFailed: Int = 0
    ) {
        data class SellerCampaignErrorCreation(
            @SerializedName("error_description")
            val errorDescription: String = "",
            @SerializedName("error_title")
            val errorTitle: String = ""
        )
    }

    data class ResponseHeader(
        @SerializedName("errorMessage")
        val errorMessages: List<String> = listOf()
    )
}