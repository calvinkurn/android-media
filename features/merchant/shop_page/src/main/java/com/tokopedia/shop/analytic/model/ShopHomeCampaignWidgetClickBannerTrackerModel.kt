package com.tokopedia.shop.analytic.model

data class ShopHomeCampaignWidgetClickBannerTrackerModel(
    val shopId: String = "",
    val userId: String = "",
    val campaignId: String = "",
    val campaignName: String = "",
    val statusCampaign: String = "",
    val widgetMasterId: String = "",
    val isFestivity: Boolean = false,
    val position: Int = 0
)
