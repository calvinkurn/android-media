package com.tokopedia.shop.analytic

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface


/**
 * Created by mzennis on 26/10/20.
 */
class ShopPlayWidgetAnalyticListener(
        private val trackingQueue: TrackingQueue,
        private val userSession: UserSessionInterface
) : PlayWidgetAnalyticListener {

    var shopId: String = ""
    var widgetId: String = ""

    private val userId: String
        get() = userSession.userId

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(view: PlayWidgetView, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click view all play",
                        EVENT_LABEL to "$shopId - Tokopedia Play - $pos",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_VIEW,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = "impression on play sgc banner",
                eventLabel = "view on banner play - $shopId - $pos",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play outside banner - p$channelPositionInList",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_CLICK,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = CLICK,
                eventLabel = "click on banner play - $shopId - $pos",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play outside banner - p$channelPositionInList",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_VIEW,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = "impression on play sgc channel",
                eventLabel = "view channel - $shopId - ${item.channelId} - $channelPositionInList - $pos - $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play inside banner - p$channelPositionInList",
                                creative = item.video.coverUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_CLICK,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = CLICK,
                eventLabel = "click channel - $shopId - ${item.channelId} - $channelPositionInList - $pos - $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play inside banner - p$channelPositionInList",
                                creative = item.video.coverUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click ${if (isRemindMe) "on remove" else ""} remind me",
                        EVENT_LABEL to "$shopId - $pos",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click other content",
                        EVENT_LABEL to "$shopId - $pos",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    private fun withWidgetPosition(onTrack: (Int) -> Unit) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
        onTrack(widgetPosition)
    }
}