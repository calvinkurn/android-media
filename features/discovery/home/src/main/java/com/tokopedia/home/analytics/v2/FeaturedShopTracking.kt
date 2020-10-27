package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object FeaturedShopTracking : BaseTracking() {
    private const val CLICK_BACKGROUND_ON_FEATURED_SHOP = "click on background dynamic channel shop"
    private const val CLICK_VIEW_ALL_ON_FEATURED_SHOP = "click view all on dynamic channel shop"
    private const val CLICK_VIEW_ALL_CARD_ON_FEATURED_SHOP = "click view all card on dynamic channel shop"

    private const val DYNAMIC_CHANNEL_SHOP = "dynamic channel shop"
    private const val BANNER_DYNAMIC_CHANNEL_SHOP = "banner dynamic channel shop"
    private const val PROMOTION_NAME_SHOP = "/ - p%s - %s - banner - %s"

    fun getFeaturedShopItemImpression(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int): Map<String, Any> {
        val baseTracker = BaseTrackerBuilder()
        return baseTracker.constructBasicPromotionView(
                event = PROMO_VIEW,
                eventAction = Action.IMPRESSION_ON.format(BANNER_DYNAMIC_CHANNEL_SHOP),
                eventCategory = Category.HOMEPAGE,
                eventLabel = "",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType+ "_" + channelModel.trackingAttributionModel.categoryId,
                                name = PROMOTION_NAME_SHOP.format(widgetPosition, DYNAMIC_CHANNEL_SHOP, channelModel.channelHeader.name),
                                creative = channelGrid.shop.id + "-" + getShopType(channelGrid.shop),
                                position = position.toString()
                        )
                )
        ).appendCurrentSite(CurrentSite.DEFAULT)
        .appendBusinessUnit(BusinessUnit.DEFAULT)
        .appendChannelId(channelModel.id)
        .appendScreen("/")
        .appendUserId(userId)
        .build()
    }

    private fun getFeaturedShopItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int) = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.PROMO_CLICK,
            eventAction = Action.CLICK_ON.format(BANNER_DYNAMIC_CHANNEL_SHOP),
            eventCategory = Category.HOMEPAGE,
            eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
            promotions = listOf(
                    BaseTrackerConst.Promotion(
                            id = channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType+ "_" + channelModel.trackingAttributionModel.categoryId,
                            name = PROMOTION_NAME_SHOP.format(widgetPosition, DYNAMIC_CHANNEL_SHOP, channelModel.channelHeader.name),
                            creative = channelGrid.shop.id + "-" + getShopType(channelGrid.shop),
                            position = position.toString()
                    )
            )
    )
            .appendChannelId(channelModel.id)
            .appendAffinity(channelModel.trackingAttributionModel.persona)
            .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
            .appendShopId(channelModel.trackingAttributionModel.brandId)
            .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendChannelId(channelModel.id)
            .appendScreen(Screen.DEFAULT)
            .appendUserId(userId)
            .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
            .build()

    fun sendFeaturedShopItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, userId: String, widgetPosition: Int, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getFeaturedShopItemClick(channelModel, channelGrid, userId, widgetPosition, position))
    }

    fun sendFeaturedShopViewAllClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedViewAllClick(channelModel.channelHeader.name, channelId, userId))
    }

    fun sendFeaturedShopViewAllCardClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedViewAllCardClick(channelModel.channelHeader.name, channelId, userId))
    }

    fun sendFeaturedShopBackgroundClick(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getFeaturedBackgroundBannerClick(channelModel.channelHeader.name, channelId, userId))
    }


    private fun getFeaturedViewAllClick(headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
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

    private fun getFeaturedViewAllCardClick(headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
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

    private fun getFeaturedBackgroundBannerClick(headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
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

    private fun getShopType(shop: ChannelShop): String{
        return if (shop.isGoldMerchant) "gold"
        else if (shop.isOfficialStore) "os"
        else "pm"
    }
}