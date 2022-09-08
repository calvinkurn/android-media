package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetSellerCampaignAttributeResponse(
    @SerializedName("getSellerCampaignAttribute")
    val getSellerCampaignAttribute: GetSellerCampaignAttribute = GetSellerCampaignAttribute()
) {
    data class GetSellerCampaignAttribute(
        @SerializedName("campaign_detail")
        val campaignDetail: List<CampaignDetail> = listOf(),
        @SerializedName("max_count_allowed")
        val maxCountAllowed: Int = 0,
        @SerializedName("remaining_campaign_quota")
        val remainingCampaignQuota: Int = 0,
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("shop_attribute")
        val shopAttribute: ShopAttribute = ShopAttribute(),
        @SerializedName("total_count")
        val totalCount: Int = 0
    ) {
        data class CampaignDetail(
            @SerializedName("campaign_id")
            val campaignId: String = "",
            @SerializedName("campaign_name")
            val campaignName: String = "",
            @SerializedName("end_date")
            val endDate: String = "",
            @SerializedName("start_date")
            val startDate: String = "",
            @SerializedName("status_id")
            val statusId: String = ""
        )

        data class ResponseHeader(
            @SerializedName("errorMessage")
            val errorMessage: List<String> = listOf(),
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )

        data class ShopAttribute(
            @SerializedName("campaign_quota")
            val campaignQuota: Int = 0,
            @SerializedName("max_campaign_duration")
            val maxCampaignDuration: Long = 0,
            @SerializedName("max_etalase")
            val maxEtalase: Int = 0,
            @SerializedName("max_overlapping_campaign")
            val maxOverlappingCampaign: Int = 0,
            @SerializedName("max_single_product_submission")
            val maxSingleProductSubmission: Int = 0,
            @SerializedName("max_upcoming_duration")
            val maxUpcomingDuration: Long = 0,
            @SerializedName("user_relation_restriction")
            val userRelationRestriction: Boolean = false,
            @SerializedName("widget_background_color")
            val widgetBackgroundColor: Boolean = false
        )
    }
}