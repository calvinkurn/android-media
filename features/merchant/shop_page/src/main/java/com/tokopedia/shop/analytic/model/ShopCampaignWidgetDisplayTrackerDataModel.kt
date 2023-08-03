package com.tokopedia.shop.analytic.model

data class ShopCampaignWidgetDisplayTrackerDataModel(
    val widgetId: String,
    val name: String,
    val position: Int,
    val shopId: String,
    val userId: String,
    val widgetTitle: String
)
