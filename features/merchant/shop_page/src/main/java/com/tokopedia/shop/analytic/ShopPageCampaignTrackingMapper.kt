package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.model.ClickSeeMoreVoucherSliderItemTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignBannerTimerTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignClickCtaSliderBannerHighlightTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignHeaderTitleTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignPlayWidgetItemTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignProductSliderBannerHighlightTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetDisplayTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetProductHighlightTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderItemClickCtaTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderItemClickTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderTrackerModel

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
        userId: String,
        widgetTitle: String
    ) = ShopCampaignWidgetDisplayTrackerDataModel(
        widgetId,
        name,
        position,
        shopId,
        userId,
        widgetTitle
    )

    fun mapToProductSliderBannerHighlightTrackerDataModel(
        widgetId: String,
        selectedTabName: String,
        position: Int,
        imageUrl: String,
        appLink: String,
        shopId: String,
        userId: String
    ) = ShopCampaignProductSliderBannerHighlightTrackerDataModel(
        widgetId,
        selectedTabName,
        position,
        imageUrl,
        appLink,
        shopId,
        userId
    )

    fun mapToClickCtaSliderBannerHighlightTrackerDataModel(
        widgetId: String,
        shopId: String,
        userId: String
    ) = ShopCampaignClickCtaSliderBannerHighlightTrackerDataModel(
        widgetId,
        shopId,
        userId
    )

    fun mapToShopCampaignPlayWidgetItemTrackerDataModel(
        campaignId: String,
        widgetId: String,
        channelId: String,
        tabName: String,
        position: Int,
        shopId: String,
        userId: String
    ) = ShopCampaignPlayWidgetItemTrackerDataModel(
        campaignId,
        widgetId,
        channelId,
        tabName,
        position,
        shopId,
        userId
    )
}
