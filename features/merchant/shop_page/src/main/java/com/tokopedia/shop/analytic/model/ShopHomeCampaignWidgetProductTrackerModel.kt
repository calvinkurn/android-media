package com.tokopedia.shop.analytic.model

data class ShopHomeCampaignWidgetProductTrackerModel(
    val shopId: String = "",
    val userId: String = "",
    val campaignId: String = "",
    val campaignName: String = "",
    val statusCampaign: String = "",
    val widgetPosition: Int = 0,
    val position: Int = 0,
    val widgetMasterId: String = "",
    val isFestivity: Boolean = false,
    val productId: String = "",
    val productName: String = "",
    val productPrice: String = ""
)
