package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.model.*

object ShopPageCampaignTrackingMapper {
    fun mapToShopCampaignBannerTimerTrackerDataModel(
        campaignId: String,
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignBannerTimerTrackerDataModel(
        campaignId,
        widgetId,
        shopId,
        userId
    )

    fun mapToShopCampaignWidgetHeaderTitleTrackerDataModel(
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignHeaderTitleTrackerDataModel(
        widgetId,
        shopId,
        userId
    )

    fun mapToShopHomeCampaignWidgetProductTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        widgetId: String,
        tabName: String,
        position: Int,
        productId: String,
        productName: String,
        productPrice: String
    ) = ShopCampaignWidgetProductHighlightTrackerModel(
        shopId = shopId,
        userId = userId,
        campaignId = campaignId,
        widgetId = widgetId,
        tabName = tabName,
        position = position,
        productId = productId,
        productName = productName,
        productPrice = productPrice
    )

    fun mapToShopCampaignVoucherSliderTrackerDataModel(
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignWidgetVoucherSliderTrackerModel(
        widgetId = widgetId,
        shopId = shopId,
        userId = userId
    )

    fun mapToClickCtaVoucherSliderItemTrackerDataModel(
        buttonStr: String,
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignWidgetVoucherSliderItemClickCtaTrackerModel(
        buttonStr,
        widgetId,
        shopId,
        userId
    )

    fun mapToClickSeeMoreVoucherSliderTrackerDataModel(
        widgetId: String,
        shopId: String,
        userId: String
    ) = ClickSeeMoreVoucherSliderItemTrackerModel(
        widgetId,
        shopId,
        userId
    )

    fun mapToClickVoucherSliderItemTrackerDataModel(
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignWidgetVoucherSliderItemClickTrackerModel(
        widgetId,
        shopId,
        userId
    )

    fun mapToShopCampaignWidgetDisplayTrackerDataModel(
        widgetId: String,
        name: String,
        position: Int,
        shopId: String,
        userId: String
    ) = ShopCampaignWidgetDisplayTrackerDataModel(
        widgetId,
        name,
        position,
        shopId,
        userId
    )
}
