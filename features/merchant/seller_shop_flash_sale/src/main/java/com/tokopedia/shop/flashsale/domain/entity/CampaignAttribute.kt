package com.tokopedia.shop.flashsale.domain.entity

data class CampaignAttribute(
    val success : Boolean,
    val errorMessage : String,
    val campaignDetail: List<CampaignDetail> = listOf(),
    val maxCountAllowed: Int = 0,
    val remainingCampaignQuota: Int = 0,
    val shopAttribute: ShopAttribute = ShopAttribute(),
    val totalCount: Int = 0
) {
    data class CampaignDetail(
        val campaignId: Long = 0,
        val campaignName: String = "",
        val endDate: String = "",
        val startDate: String = "",
        val statusId: Int = 0
    )

    data class ShopAttribute(
        val campaignQuota: Int = 0,
        val maxCampaignDuration: Long = 0,
        val maxEtalase: Int = 0,
        val maxOverlappingCampaign: Int = 0,
        val maxSingleProductSubmission: Int = 0,
        val maxUpcomingDuration: Long = 0,
        val userRelationRestriction: Boolean = false,
        val widgetBackgroundColor: Boolean = false
    )
}
