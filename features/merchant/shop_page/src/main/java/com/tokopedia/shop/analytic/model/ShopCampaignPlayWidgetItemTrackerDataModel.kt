package com.tokopedia.shop.analytic.model

data class ShopCampaignPlayWidgetItemTrackerDataModel(
    val campaignId: String,
    val widgetId: String,
    val channelId: String,
    val tabName: String,
    val position: Int,
    val shopId: String,
    val userId: String
)
