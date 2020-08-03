package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

object LegoBannerTracking : BaseTracking() {
    private const val CLICK_ON_LEGO_3 = "lego banner 3 image click"
    private const val CLICK_ON_LEGO_4 = "lego banner 4 image click"
    private const val CLICK_ON_LEGO_6 = "lego banner click"

    private const val CLICK_VIEW_ALL_ON_LEGO_6 = "lego banner click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_4 = "lego banner 4 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_3 = "lego banner 3 image click view all"

    private const val IMPRESSION_HOME_BANNER = "home banner impression"

    fun getHomeBannerImpression(promotionObjects: List<Any>) = getBasicPromotionChannelView(
            event = PROMO_VIEW,
            eventAction = IMPRESSION_HOME_BANNER,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "",
            channelId = "",
            userId = "",
            promotionObject = promotionObjects
    )

    fun sendLegoBannerSixClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerSixClick(channelModel, channelGrid, position))
    }

    fun sendLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerFourClick(channelModel, channelGrid, position))
    }

    fun sendLegoBannerThreeClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerThreeClick(channelModel, channelGrid, position))
    }

    fun sendLegoBannerSixClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerSixViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendLegoBannerFourClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerFourViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendLegoBannerThreeClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerThreeViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    private fun getLegoBannerSixViewAllClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL_ON_LEGO_6,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Label.ATTRIBUTION_LABEL, channelModel.channelBanner.attribution,
            Label.AFFINITY_LABEL, channelModel.trackingAttributionModel.persona,
            Label.CATEGORY_LABEL, channelModel.trackingAttributionModel.categoryId,
            Label.SHOP_LABEL, channelModel.trackingAttributionModel.brandId,
            Label.CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>

    private fun getLegoBannerFourViewAllClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL_ON_LEGO_4,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Label.ATTRIBUTION_LABEL, channelModel.channelBanner.attribution,
            Label.AFFINITY_LABEL, channelModel.trackingAttributionModel.persona,
            Label.CATEGORY_LABEL, channelModel.trackingAttributionModel.categoryId,
            Label.SHOP_LABEL, channelModel.trackingAttributionModel.brandId,
            Label.CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>

    private fun getLegoBannerThreeViewAllClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String) =  DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CLICK_VIEW_ALL_ON_LEGO_3,
            Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelId, headerName),
            ChannelId.KEY, channelId,
            Label.ATTRIBUTION_LABEL, channelModel.channelBanner.attribution,
            Label.AFFINITY_LABEL, channelModel.trackingAttributionModel.persona,
            Label.CATEGORY_LABEL, channelModel.trackingAttributionModel.categoryId,
            Label.SHOP_LABEL, channelModel.trackingAttributionModel.brandId,
            Label.CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode,
            Screen.KEY, Screen.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT
    ) as HashMap<String, Any>

    private fun getLegoBannerSixClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CLICK_ON_LEGO_6,
            eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
            channelId = channelModel.id,
            affinity = channelModel.trackingAttributionModel.persona,
            categoryId = channelModel.trackingAttributionModel.categoryPersona,
            shopId = channelModel.trackingAttributionModel.brandId,
            campaignCode = channelModel.trackingAttributionModel.campaignCode,
            promotions = listOf(
                    channelGrid.convertToHomePromotionModel(channelModel, position.toString())
            ),
            attribution = channelModel.trackingAttributionModel.galaxyAttribution
    )

    private fun getLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CLICK_ON_LEGO_4,
            eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
            channelId = channelModel.id,
            affinity = channelModel.trackingAttributionModel.persona,
            categoryId = channelModel.trackingAttributionModel.categoryPersona,
            shopId = channelModel.trackingAttributionModel.brandId,
            campaignCode = channelModel.trackingAttributionModel.campaignCode,
            promotions = listOf(
                    channelGrid.convertToHomePromotionModel(channelModel, position.toString())
            ),
            attribution = channelModel.trackingAttributionModel.galaxyAttribution
    )

    private fun getLegoBannerThreeClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CLICK_ON_LEGO_3,
            eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
            channelId = channelModel.id,
            affinity = channelModel.trackingAttributionModel.persona,
            categoryId = channelModel.trackingAttributionModel.categoryPersona,
            shopId = channelModel.trackingAttributionModel.brandId,
            campaignCode = channelModel.trackingAttributionModel.campaignCode,
            promotions = listOf(
                    channelGrid.convertToHomePromotionModel(channelModel, position.toString())
            ),
            attribution = channelModel.trackingAttributionModel.galaxyAttribution
    )
}