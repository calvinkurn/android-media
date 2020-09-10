package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

object FeaturedShopTracking : BaseTracking() {
    private const val CLICK_BACKGROUND_ON_FEATURED_SHOP = "click background on dynamic channel shop"
    private const val CLICK_VIEW_ALL_ON_FEATURED_SHOP = "click view all on dynamic channel shop"
    private const val CLICK_VIEW_ALL_CARD_ON_FEATURED_SHOP = "click view all card on dynamic channel shop"

    private const val DYNAMIC_CHANNEL_SHOP = "dynamic channel shop"
    private const val PROMOTION_NAME_SHOP = "/ - p%s - %s - banner - %s"

    fun getFeaturedShopItemImpression(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int) = getBasicPromotionChannelView(
            event = PROMO_VIEW,
            eventAction = Action.IMPRESSION_ON.format(DYNAMIC_CHANNEL_SHOP),
            eventCategory = Category.HOMEPAGE,
            eventLabel = "",
            channelId = "",
            screen = "/",
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            userId = userId,
            promotionObject = listOf(
                Promotion(
                        id = channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType+ "_" + channelModel.trackingAttributionModel.categoryId,
                        name = PROMOTION_NAME_SHOP.format(widgetPosition, DYNAMIC_CHANNEL_SHOP, channelModel.channelHeader.name),
                        creative = channelGrid.shop.id + "-" + channelGrid.shopBadgeUrl,
                        position = position.toString()
                )
            )
    )

    private fun getFeaturedShopItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventAction = Action.CLICK_ON.format(DYNAMIC_CHANNEL_SHOP),
            eventCategory = Category.HOMEPAGE,
            eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
            channelId = channelModel.id,
            affinity = channelModel.trackingAttributionModel.persona,
            categoryId = channelModel.trackingAttributionModel.categoryPersona,
            shopId = channelModel.trackingAttributionModel.brandId,
            campaignCode = channelModel.trackingAttributionModel.campaignCode,
            userId = userId,
            promotions = listOf(
                    Promotion(
                            id = channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType+ "_" + channelModel.trackingAttributionModel.categoryId,
                            name = PROMOTION_NAME_SHOP.format(widgetPosition, DYNAMIC_CHANNEL_SHOP, channelModel.channelHeader.name),
                            creative = channelModel.trackingAttributionModel.categoryId + "-" + channelGrid.shopBadgeUrl,
                            position = position.toString()
                    )
            ),
            attribution = channelModel.trackingAttributionModel.galaxyAttribution
    )

    fun sendFeaturedShopItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getFeaturedShopItemClick(channelModel, channelGrid, userId, widgetPosition, position))
    }

    fun sendFeaturedShopViewAllClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendFeaturedShopViewAllCardClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedViewAllCardClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendFeaturedShopBackgroundClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedBackgroundBannerClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }


    private fun getFeaturedViewAllClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL_ON_FEATURED_SHOP,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>

    private fun getFeaturedViewAllCardClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL_CARD_ON_FEATURED_SHOP,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>

    private fun getFeaturedBackgroundBannerClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_BACKGROUND_ON_FEATURED_SHOP,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>
}