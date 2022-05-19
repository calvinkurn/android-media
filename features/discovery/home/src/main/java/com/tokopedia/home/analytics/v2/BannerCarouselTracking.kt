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

    private const val IMPRESSION_ON_BANNER_CAROUEL = "slider banner impression"
    private const val CLICK_ON_BANNER_CAROUSEL = "slider banner click"
    private const val DEFAULT_VALUE_HEADER_NAME = "default"
    private const val EVENT_ACTION_CLICK_VIEW_ALL = "slider banner click view all"
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
                    eventLabel = Value.EMPTY,
                    promotions = listOf(channelGrid.convertToHomePromotionModel(channelModel, position)))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build()
    }

    fun getBannerCarouselItemImpression(channel: ChannelModel, grid: ChannelGrid, position: Int, isToIris: Boolean = false, userId: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_ON_BANNER_CAROUEL,
                eventLabel = Label.NONE,
                promotions = listOf(grid.convertToHomePromotionModel(channel, position)))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build() as HashMap<String, Any>
    }

    fun getBannerCarouselImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_ON_BANNER_CAROUEL,
                eventLabel = Label.NONE,
                promotions = channel.convertToHomePromotionModelList(position))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build() as HashMap<String, Any>
    }

    fun sendBannerCarouselSeeAllClick() {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
                event = CLICK_HOMEPAGE,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_CLICK_VIEW_ALL,
                eventLabel = Label.NONE)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    fun ChannelGrid.convertToHomePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
            id = id,
            name = channelModel.trackingAttributionModel.promoName,
            creative = attribution,
            position = (position + 1).toString()
    )

    fun ChannelModel.convertToHomePromotionModelList(position: Int) =
            this.channelGrids.map { it.convertToHomePromotionModel(this, position) }
}