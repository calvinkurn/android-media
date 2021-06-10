package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

object BannerCarouselTracking : BaseTrackerConst() {

    private const val BANNER_CAROUSEL_IMAGE_NAME = "dynamic channel carousel"
    private const val CLICK_ON_BANNER_CAROUSEL = "dynamic channel carousel click"
    private const val DEFAULT_VALUE_HEADER_NAME = "default"
    private const val EVENT_ACTION_CLICK_VIEW_ALL = "click view all on dynamic channel carousel"
    private const val EVENT_LABEL_CLICK_VIEW_ALL = "%s - %s"

    fun sendBannerCarouselClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getBannerCarouselClick(channelModel, channelGrid, position, userId))
    }

    fun sendBannerCarouselImpression(trackingQueue: TrackingQueue, channel: ChannelModel, position: Int, isToIris: Boolean = false) {
        trackingQueue.putEETracking(getBannerCarouselImpression(channel, position, isToIris))
    }

    private fun getBannerCarouselClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_ON_BANNER_CAROUSEL,
                    eventLabel = Value.FORMAT_2_ITEMS_DASH.format(channelModel.id,
                            if (channelModel.channelHeader.name.isNotEmpty()) channelModel.channelHeader.name
                            else DEFAULT_VALUE_HEADER_NAME
                    ),
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendAttribution(channelModel.trackingAttributionModel.galaxyAttribution)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCampaignCode(
                        if (channelGrid.campaignCode.isNotEmpty()) channelGrid.campaignCode
                        else channelModel.trackingAttributionModel.campaignCode)
                .appendChannelId(channelModel.id)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendAffinity(channelModel.trackingAttributionModel.persona)
                .appendCategoryId(channelModel.trackingAttributionModel.categoryPersona)
                .appendShopId(channelModel.trackingAttributionModel.brandId)
                .appendUserId(userId)
                .build()
    }

    fun getBannerCarouselItemImpression(channel: ChannelModel, grid: ChannelGrid, position: Int, isToIris: Boolean = false, userId: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(BANNER_CAROUSEL_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = listOf(grid.convertToHomePromotionModel(channel, position)))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendChannelId(channel.id)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build() as HashMap<String, Any>
    }

    fun getBannerCarouselImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(BANNER_CAROUSEL_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.convertToHomePromotionModelList(position))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendChannelId(channel.id)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build() as HashMap<String, Any>
    }

    fun sendBannerCarouselSeeAllClick(channelModel: ChannelModel, userId: String) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
                event = CLICK_HOMEPAGE,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_CLICK_VIEW_ALL,
                eventLabel = String.format(
                        EVENT_LABEL_CLICK_VIEW_ALL,
                        channelModel.id,
                        channelModel.channelHeader.name
                ))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendChannelId(channelModel.id)
                .appendCurrentSite(CurrentSite.DEFAULT)
        if (userId.isNotEmpty()) trackerBuilder.appendUserId(userId)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    fun ChannelGrid.convertToHomePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
            id = channelModel.id + "_" + id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
            name = channelModel.trackingAttributionModel.promoName,
            creative = attribution,
            position = (position + 1).toString()
    )

    fun ChannelModel.convertToHomePromotionModelList(position: Int) =
            this.channelGrids.map { it.convertToHomePromotionModel(this, position) }
}