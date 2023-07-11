package com.tokopedia.shop.analytic.model

data class ShopCampaignProductSliderBannerHighlightTrackerDataModel(
    val widgetId: String,
    val selectedTabName: String,
    val position: Int,
    val imageUrl: String,
    val appLink: String,
    val shopId: String,
    val userId: String
){
    fun getProductIdFromAppLink(): String {
        return appLink.split("/").lastOrNull().orEmpty()
    }
}
