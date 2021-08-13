package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.v2.BaseTracking.Action.CLICK_ON
import com.tokopedia.home.analytics.v2.BaseTracking.Action.IMPRESSION_ON
import com.tokopedia.home.analytics.v2.BaseTracking.Category.HOMEPAGE
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_CLICK
import com.tokopedia.home.analytics.v2.BaseTracking.Event.PROMO_VIEW
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

/**
 * Created by Lukas on 23/11/20.
 */
object CategoryNavigationTracking : BaseTracking(){
    private const val CATEGORY_ICON = "category icon"
    fun sendCategoryNavigationClick(channel: ChannelModel, grid: ChannelGrid, userId: String, position: Int){

        val tracker = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_CLICK,
                eventCategory = HOMEPAGE,
                eventAction = CLICK_ON.format(CATEGORY_ICON),
                eventLabel = grid.name,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = "${channel.id}_${grid.id}",
                                name = "/ - $CATEGORY_ICON",
                                creative = "",
                                position = (position + 1).toString())
                ))
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendShopId(grid.shopId)
                .appendCategoryId(channel.trackingAttributionModel.categoryPersona)
                .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                .appendAffinity(channel.trackingAttributionModel.persona)
                .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT).build()
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }

    fun sendCategoryNavigationImpress(trackingQueue: TrackingQueue, channel: ChannelModel, grid: ChannelGrid, userId: String, position: Int){
        val tracking = BaseTrackerBuilder().constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = HOMEPAGE,
                eventAction = IMPRESSION_ON.format(CATEGORY_ICON),
                eventLabel = Label.NONE,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = "${channel.id}_${grid.id}",
                                name = "/ - $CATEGORY_ICON",
                                creative = "",
                                position = (position + 1).toString())
                ))
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT).build()
        trackingQueue.putEETracking(tracking as HashMap<String, Any>)
    }
}
