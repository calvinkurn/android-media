package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.HomePageTracking.FORMAT_4_VALUE_UNDERSCORE
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW

object LegoBannerTracking : BaseTrackerConst() {
    private const val CLICK_ON_LEGO_3 = "lego banner 3 image click"
    private const val CLICK_ON_LEGO_4 = "lego banner 4 image click"
    private const val CLICK_ON_LEGO_2 = "lego banner 2 image click"
    private const val CLICK_ON_LEGO_6 = "lego banner click"
    private const val CLICK_ON_LEGO_6_AUTO = "lego banner 6 auto click"

    private const val CLICK_VIEW_ALL_ON_LEGO_6 = "lego banner click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_6_AUTO = "lego banner 6 auto click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_4 = "lego banner 4 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_3 = "lego banner 3 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_2 = "lego banner 2 image click view all"

    private const val IMPRESSION_HOME_BANNER = "home banner impression"

    private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"
    private const val LEGO_BANNER_3_IMAGE_NAME = "lego banner 3 image"
    private const val LEGO_BANNER_2_IMAGE_NAME = "lego banner 2 image"
    private const val LEGO_BANNER_6_IMAGE_NAME = "lego banner"
    private const val LEGO_BANNER_6_AUTO_IMAGE_NAME = "lego banner 6 auto"

    fun getLegoImpression(
        channelModel: ChannelModel,
        userId: String
    ): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
            event = PROMO_VIEW,
            eventAction = IMPRESSION_HOME_BANNER,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "",
            promotions = getLegoPromotionList(channelModel)
        )
            .appendUserId(userId)
            .build() as HashMap<String, Any>
    }

    private fun getLegoPromotionList(channelModel: ChannelModel): List<Promotion> {
        val tracking = channelModel.trackingAttributionModel
        return channelModel.channelGrids.mapIndexed { i, grid ->
            Promotion(
                id = "%s_%s_%s_%s".format(channelModel.id, grid.id, tracking.persoType, tracking.categoryId),
                name = tracking.promoName,
                creative = grid.attribution,
                position = (i + 1).toString()
            )
        }
    }

    fun sendLegoBannerTwoClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerTwoClick(channelModel, channelGrid, position, userId))
    }

    fun sendLegoBannerSixClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerSixClick(channelModel, channelGrid, position, userId))
    }

    fun sendLegoBannerSixAutoClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerSixAutoClick(channelModel, channelGrid, position))
    }

    fun sendLegoBannerTallSixAutoClick(channelModel: ChannelModel, parentPosition: Int) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerTallSixAutoClick(channelModel, parentPosition))
    }

    fun sendLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerFourClick(channelModel, channelGrid, position, userId))
    }

    fun sendLegoBannerThreeClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getLegoBannerThreeClick(channelModel, channelGrid, position, userId))
    }

    fun sendLegoBannerSixClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerSixViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendLegoBannerSixAutoClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerSixViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId, true))
    }

    fun sendLegoBannerFourClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerFourViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendLegoBannerThreeClickViewAll(channelModel: ChannelModel, channelId: String, userId: String) {
        getTracker().sendGeneralEvent(getLegoBannerThreeViewAllClick(channelModel, channelModel.channelHeader.name, channelId, userId))
    }

    fun sendLegoBannerTwoClickViewAll(channelModel: ChannelModel) {
        val tracking = getLegoBannerTwoViewAllClick(channelModel, channelModel.channelHeader.name)
        getTracker().sendEnhanceEcommerceEvent(tracking.first, tracking.second)
    }

    private fun getLegoBannerSixViewAllClick(channelModel: ChannelModel, headerName: String, channelId: String, userId: String, isAuto: Boolean = false): HashMap<String, Any> {
        val legoSixClickAllActionName = if (isAuto) CLICK_VIEW_ALL_ON_LEGO_6_AUTO else CLICK_VIEW_ALL_ON_LEGO_6
        return DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, legoSixClickAllActionName,
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
    }

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

    private fun getLegoBannerTwoViewAllClick(channelModel: ChannelModel, headerName: String) : Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, CLICK_VIEW_ALL_ON_LEGO_2)
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, headerName))
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CampaignCode.KEY, channelModel.trackingAttributionModel.campaignCode)
        bundle.putString(ChannelId.KEY, channelModel.id)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        return Pair(Event.CLICK_HOMEPAGE, bundle)
    }

    private fun getLegoBannerSixClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        val legoSixClickActionName = CLICK_ON_LEGO_6
        return trackerBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = legoSixClickActionName,
                    eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendUserId(userId)
                .build()
    }

    private fun getLegoBannerSixAutoClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        val legoSixClickActionName = CLICK_ON_LEGO_6_AUTO
        val promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, (position+1)))

        return trackerBuilder.constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = legoSixClickActionName,
                eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                promotions = promotions)
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .build()
    }

    private fun getLegoBannerTwoClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_ON_LEGO_2,
                eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)

                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    private fun getLegoBannerTallSixAutoClick(channelModel: ChannelModel, parentPosition: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        val legoSixClickActionName = CLICK_ON_LEGO_6_AUTO
        val promotions = listOf(channelModel.channelBanner.convertToHomePromotionModel(channelModel, parentPosition))
        return trackerBuilder.constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = legoSixClickActionName,
                eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
                promotions = promotions)
                .appendChannelId(channelModel.id)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build()
    }

    private fun getLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String): Map<String, Any> {
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
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendUserId(userId).build()
    }

    private fun getLegoBannerThreeClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String): Map<String, Any> {
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
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendUserId(userId).build()
    }

    fun getLegoBannerFourImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String): Map<String, Any> {
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
                .appendUserId(userId)
                .build()
    }

    fun getLegoBannerThreeImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String): Map<String, Any> {
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
                .appendUserId(userId)
                .build()
    }

    fun getLegoBannerSixImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String = ""): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        val legoSixImageName = LEGO_BANNER_6_IMAGE_NAME
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(legoSixImageName),
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
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
    }

    fun getLegoBannerTwoImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_2_IMAGE_NAME),
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
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendUserId(userId)
                .build()
    }

    fun ChannelGrid.convertToHomePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
            id = channelModel.id + "_" + id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
            name = channelModel.trackingAttributionModel.promoName,
            creative = attribution,
            position = (position + 1).toString()
    )

    fun ChannelBanner.convertToHomePromotionModel(channelModel: ChannelModel, parentPosition: Int): Promotion {
        val PROMO_NAME_LEGO_6_AUTO_IMAGE = "/ - p%s - lego banner 6 auto - %s - %s"
        val tallBannerPromoName =
                String.format(PROMO_NAME_LEGO_6_AUTO_IMAGE, parentPosition.toString(), "tall_creative", channelModel.channelHeader.name)
        return Promotion(
                id = channelModel.id + "_" + id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
                name = tallBannerPromoName,
                creative = "",
                position = "1"
        )
    }

    fun convertLegoSixAutoBannerDataLayerForCombination(channels: DynamicHomeChannel.Channels, position: Int): List<Any> {
        val PROMO_NAME_LEGO_6_AUTO_IMAGE = "/ - p%s - lego banner 6 auto - %s - %s"
        val ID_LEGO_6_AUTO_IMAGE = "%s_%s_%s_%s"
        val list: MutableList<Any> = ArrayList()
        val tallBannerPromoName =
                String.format(PROMO_NAME_LEGO_6_AUTO_IMAGE, position.toString(), "tall_creative", channels.header.name)
        //Six lego auto is sampled by banner
        list.add(
                DataLayer.mapOf(
                        "id", String.format(ID_LEGO_6_AUTO_IMAGE, channels.id, channels.banner.id, channels.persoType, channels.categoryID),
                        "name", tallBannerPromoName,
                        "creative", channels.banner.attribution,
                        "creative_url", "",
                        "position", "1")
        )
        return list
    }

}
