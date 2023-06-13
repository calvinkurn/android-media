package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductSubmissionProgressResponse(
    @SerializedName("getFlashSaleProductSubmissionProgress")
    val getFlashSaleProductSubmissionProgress: GetFlashSaleProductSubmissionProgress = GetFlashSaleProductSubmissionProgress()
) {
    data class GetFlashSaleProductSubmissionProgress(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("type")
        val type: String = "",
        @SerializedName("campaign_list")
        val listCampaign: List<Campaign> = listOf(),
        @SerializedName("campaign_product_error_list")
        val listCampaignProductError: List<CampaignProductError> = listOf(),
        @SerializedName("open_sse")
        val openSse: Boolean = false,
        @SerializedName("list_product_has_next")
        val listProductHasNext: Boolean = false
    ) {
        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: Int = 0,
            @SerializedName("error_message")
            val errorMessage: List<String> = listOf(),
            @SerializedName("process_time")
            val processTime: Int = 0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )

        data class Campaign(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("campaign_name")
            val campaignName: String = "",
            @SerializedName("product_processed")
            val productProcessed: Int = 0,
            @SerializedName("product_submitted")
            val productSubmitted: Int = 0,
            @SerializedName("campaign_picture")
            val campaignPicture: String = ""
        )

        data class CampaignProductError(
            @SerializedName("product_id")
            val productId: String = "",
            @SerializedName("product_name")
            val productName: String = "",
            @SerializedName("sku")
            val sku: String = "",
            @SerializedName("product_picture")
            val productPicture: String = "",
            @SerializedName("message")
            val message: String = "",
            @SerializedName("error_type")
            val errorType: String = ""
        )
    }
}
