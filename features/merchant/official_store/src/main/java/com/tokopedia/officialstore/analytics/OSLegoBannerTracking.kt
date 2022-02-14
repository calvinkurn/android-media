package com.tokopedia.officialstore.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by yfsx on 09/11/21.
 */
object OSLegoBannerTracking: BaseTrackerConst() {

    private const val CLICK_ON_LEGO_4 = "lego banner 4 image click"
    private const val CLICK_ON_LEGO_2 = "lego banner 2 image click"

    private const val CLICK_VIEW_ALL_ON_LEGO_6_AUTO = "lego banner 6 auto click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_4 = "lego banner 4 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_2 = "lego banner 2 image click view all"
    private const val CLICK_VIEW_ALL_ON_LEGO_3 = "lego banner 3 image click view all"

    private const val IMPRESSION_HOME_BANNER = "home banner impression"

    private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"
    private const val LEGO_BANNER_3_IMAGE_NAME = "lego banner 3 image"
    private const val LEGO_BANNER_2_IMAGE_NAME = "lego banner 2 image"
    private const val LEGO_BANNER_6_IMAGE_NAME = "lego banner"
    private const val LEGO_BANNER_6_AUTO_IMAGE_NAME = "lego banner 6 auto"
    private const val PROMOTION_NAME_CLICK = "/ - p%s - dynamic channel mix - banner - %s"
    private const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s"

    fun getLegoBannerTwoImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String): Map<String, Any> {
        return getLegoBannerImageImpression(channel, position, isToIris, userId, LEGO_BANNER_2_IMAGE_NAME)
    }

    fun getLegoBannerTwoClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        return getLegoBannerClick(channelModel, channelGrid, position, userId, CLICK_ON_LEGO_2)
    }

    fun getLegoBannerTwoViewAllClick(channelModel: ChannelModel, userId: String) : Map<String, Any> {
        return getLegoBannerViewAllClick(channelModel, userId, CLICK_VIEW_ALL_ON_LEGO_2)
    }

    fun getLegoBannerFourImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String): Map<String, Any> {
        return getLegoBannerImageImpression(channel, position, isToIris, userId, LEGO_BANNER_4_IMAGE_NAME)
    }

    fun getLegoBannerFourClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        return getLegoBannerClick(channelModel, channelGrid, position, userId, CLICK_ON_LEGO_4)
    }
    fun getLegoBannerFourViewAllClick(channelModel: ChannelModel, userId: String) : Map<String, Any> {
        return getLegoBannerViewAllClick(channelModel, userId, CLICK_VIEW_ALL_ON_LEGO_4)
    }

    private fun getLegoBannerImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false, userId: String, componentName: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
            event = if (isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = Action.IMPRESSION.format(componentName),
            eventLabel = Label.NONE,
            promotions = channel.channelGrids.mapIndexed { index, grid ->
                Promotion(
                    id = FORMAT_4_VALUE_UNDERSCORE.format(
                        channel.id,
                        grid.id,
                        channel.trackingAttributionModel.persoType,
                        channel.trackingAttributionModel.categoryId
                    ),
                    creative = grid.attribution,
                    name = Ecommerce.PROMOTION_NAME.format(
                        position,
                        componentName,
                        channel.channelHeader.name
                    ),
                    position = (index + 1).toString()
                )
            })
            .appendChannelId(channel.id)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendUserId(userId)
            .build()
    }


    private fun getLegoBannerClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String, clickName: String) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = clickName,
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

    private fun getLegoBannerViewAllClick(channelModel: ChannelModel, userId: String, clickName: String) =  DataLayer.mapOf(
        Event.KEY, Event.CLICK_HOMEPAGE,
        Category.KEY, Category.HOMEPAGE,
        Action.KEY, clickName,
        Label.KEY, Value.FORMAT_2_ITEMS_DASH.format(channelModel.id, channelModel.channelHeader.name),
        ChannelId.KEY, channelModel.id,
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

    private fun ChannelGrid.convertToHomePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
        id = channelModel.id + "_" + id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
        name = PROMOTION_NAME_CLICK.format(position+1, channelModel.channelHeader.name),
        creative = attribution,
        position = (position + 1).toString()
    )

}