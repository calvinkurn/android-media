package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.FORMAT_4_VALUE_UNDERSCORE
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW

object LegoBannerTracking : BaseTrackerConst() {
    private const val CLICK_ON_LEGO_3 = "lego banner 3 image click"
    private const val CLICK_ON_LEGO_4 = "lego banner 4 image click"
    private const val CLICK_ON_LEGO_6 = "lego banner click"

    private const val CLICK_VIEW_ALL_ON_LEGO_6 = "lego banner click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_4 = "lego banner 4 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_3 = "lego banner 3 image click view all"

    private const val IMPRESSION_HOME_BANNER = "home banner impression"

    private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"
    private const val LEGO_BANNER_3_IMAGE_NAME = "lego banner 3 image"
    private const val LEGO_BANNER_6_IMAGE_NAME = "lego banner"


    fun getHomeBannerImpression(promotionObjects: List<Any>) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder
                .appendEvent(PROMO_VIEW)
                .appendEventAction(IMPRESSION_HOME_BANNER)
                .appendEventCategory(Category.HOMEPAGE)
                .appendEventLabel("")
                .appendChannelId("")
                .appendUserId("")
                .appendCustomKeyValue(Ecommerce.KEY, Ecommerce.getEcommerceObjectPromoView(promotionObjects))
                .build()
    }

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

    private fun getLegoBannerSixClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_ON_LEGO_6,
                    eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .build()
    }

    private fun getLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_ON_LEGO_4,
                    eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution).build()
    }

    private fun getLegoBannerThreeClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_ON_LEGO_3,
                    eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution).build()
    }

    fun getLegoBannerFourImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                Promotion(
                        id = FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                        creative = grid.attribution,
                        name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.channelHeader.name),
                        position = (index + 1).toString()
                )})
                .appendChannelId(channel.id)
                .build()
    }

    fun getLegoBannerThreeImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_3_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                    Promotion(
                            id = FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_3_IMAGE_NAME, channel.channelHeader.name),
                            position = (index + 1).toString()
                    )
                })
                .appendChannelId(channel.id)
                .build()
    }

    fun getLegoBannerSixImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_6_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                    Promotion(
                            id = FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_6_IMAGE_NAME, channel.channelHeader.name),
                            position = (index + 1).toString()
                    )
                })
                .appendChannelId(channel.id)
                .build()
    }

    fun ChannelGrid.convertToHomePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
            id = channelModel.id + "_" + id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
            name = channelModel.trackingAttributionModel.promoName,
            creative = attribution,
            position = (position + 1).toString()
    )
}