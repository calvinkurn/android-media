package com.tokopedia.shop.analytic.model

data class ShopCampaignWidgetProductHighlightTrackerModel(
    val shopId: String,
    val userId: String,
    val campaignId: String,
    val widgetId: String,
    val tabName: String,
    val position: Int,
    val productId: String,
    val productName: String,
    val productPrice: String
)
