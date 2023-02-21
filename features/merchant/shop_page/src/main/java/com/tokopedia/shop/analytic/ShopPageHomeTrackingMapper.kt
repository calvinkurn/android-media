package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.model.*

object ShopPageHomeTrackingMapper {
    fun mapToProductShopDecorationTrackerDataModel(
        shopId: String,
        userId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        verticalPosition: Int,
        horizontalPosition: Int,
        widgetName: String,
        widgetOption: Int,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ProductShopDecorationTrackerDataModel {
        return ProductShopDecorationTrackerDataModel(
            shopId = shopId,
            userId = userId,
            productName = productName,
            productId = productId,
            productDisplayedPrice = productDisplayedPrice,
            verticalPosition = verticalPosition,
            horizontalPosition = horizontalPosition,
            widgetName = widgetName,
            widgetOption = widgetOption,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCarouselProductWidgetClickCtaTrackerModel(
        shopId: String,
        userId: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ShopHomeCarouselProductWidgetClickCtaTrackerModel {
        return ShopHomeCarouselProductWidgetClickCtaTrackerModel(
            shopId = shopId,
            userId = userId,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCampaignWidgetImpressionTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        campaignName: String,
        statusCampaign: String,
        position: Int,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ShopHomeCampaignWidgetImpressionTrackerModel {
        return ShopHomeCampaignWidgetImpressionTrackerModel(
            shopId = shopId,
            userId = userId,
            campaignId = campaignId,
            campaignName = campaignName,
            statusCampaign = statusCampaign,
            position = position,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCampaignWidgetProductTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        campaignName: String,
        statusCampaign: String,
        widgetPosition: Int,
        position: Int,
        widgetMasterId: String,
        isFestivity: Boolean,
        productId: String,
        productName: String,
        productPrice: String
    ): ShopHomeCampaignWidgetProductTrackerModel {
        return ShopHomeCampaignWidgetProductTrackerModel(
            shopId = shopId,
            userId = userId,
            campaignId = campaignId,
            campaignName = campaignName,
            statusCampaign = statusCampaign,
            widgetPosition = widgetPosition,
            position = position,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity,
            productId = productId,
            productName = productName,
            productPrice = productPrice
        )
    }

    fun mapToShopHomeCampaignWidgetClickCtaSeeAllTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        campaignName: String,
        statusCampaign: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ShopHomeCampaignWidgetClickCtaSeeAllTrackerModel {
        return ShopHomeCampaignWidgetClickCtaSeeAllTrackerModel(
            shopId = shopId,
            userId = userId,
            campaignId = campaignId,
            campaignName = campaignName,
            statusCampaign = statusCampaign,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCampaignWidgetClickRemindMeTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        campaignName: String,
        statusCampaign: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ShopHomeCampaignWidgetClickReminderTrackerModel {
        return ShopHomeCampaignWidgetClickReminderTrackerModel(
            shopId = shopId,
            userId = userId,
            campaignId = campaignId,
            campaignName = campaignName,
            statusCampaign = statusCampaign,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCampaignWidgetClickBannerTrackerModel(
        shopId: String,
        userId: String,
        campaignId: String,
        campaignName: String,
        statusCampaign: String,
        widgetMasterId: String,
        isFestivity: Boolean,
        position: Int
    ): ShopHomeCampaignWidgetClickBannerTrackerModel {
        return ShopHomeCampaignWidgetClickBannerTrackerModel(
            shopId = shopId,
            userId = userId,
            campaignId = campaignId,
            campaignName = campaignName,
            statusCampaign = statusCampaign,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity,
            position = position
        )
    }
}
