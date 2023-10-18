package com.tokopedia.shop.analytic.model

data class ShopHomeCampaignWidgetImpressionTrackerModel(
    val shopId: String = "",
    val userId: String = "",
    val campaignId: String = "",
    val campaignName: String = "",
    val statusCampaign: String = "",
    val position: Int = 0,
    val widgetMasterId: String = "",
    val isFestivity: Boolean = false
)
