package com.tokopedia.home.analytics.v2

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.analytics.v2.BaseTracking.BusinessUnit.ADS_SOLUTION
import com.tokopedia.home.analytics.v2.BaseTracking.Event.CLICK_HOMEPAGE
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue

class HomePlayWidgetAnalyticListener(
        private val trackingQueue: TrackingQueue?,
        private val userId: String
) : BaseTracking(), PlayWidgetAnalyticListener {

    var widgetId: String = ""
    var widgetName: String = ""

    private var mWidgetPosition: Int = RecyclerView.NO_POSITION

    fun setWidgetPosition(value: Int) {
        this.mWidgetPosition = if (value <= 2) 0 else 1 // following the old implementation
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click view all",
                        Label.KEY to "0 - Tokopedia Play - $pos",
                        BusinessUnit.KEY to ADS_SOLUTION,
                        CurrentSite.KEY to CurrentSite.DEFAULT,
                        UserId.KEY to userId
                )
        )
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = "homepage-cmp",
                eventAction = "impression on play sgc banner",
                eventLabel = "${item.imageUrl} - $channelPositionInList",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$pos - play sgc banner - $widgetName",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(widgetId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetMediumOverlayUiModel, channelPositionInList: Int) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = "homepage-cmp",
                eventAction = Event.CLICK,
                eventLabel = "click on banner play - ${item.imageUrl} - $channelPositionInList",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$pos - play sgc banner - $widgetName",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(widgetId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = "homepage-cmp",
                eventAction = "impression on play sgc channel",
                eventLabel = "${item.partner.id} - ${item.channelId} - $channelPositionInList - $pos - $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$pos - play sgc channel - ${item.title}",
                                creative = item.video.coverUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(item.channelId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = "homepage-cmp",
                eventAction = Event.CLICK,
                eventLabel = "click channel - ${item.partner.id} - ${item.channelId} - $channelPositionInList - $pos - $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$pos - play sgc channel - ${item.title}",
                                creative = item.video.coverUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendChannelId(item.channelId)
                .appendUserId(userId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click ${if (!isRemindMe && userId.isNotBlank()) "on remove " else ""}remind me",
                        Label.KEY to "${item.channelId} - $channelPositionInList",
                        UserId.KEY to userId
                )
        )
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetMediumBannerUiModel, channelPositionInList: Int) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click other content",
                        Label.KEY to "${item.imageUrl} - $channelPositionInList",
                        BusinessUnit.KEY to ADS_SOLUTION,
                        CurrentSite.KEY to CurrentSite.DEFAULT,
                        UserId.KEY to userId
                )
        )
    }

    private fun withWidgetPosition(onTrack: (Int) -> Unit) {
        if (mWidgetPosition == RecyclerView.NO_POSITION) return
        onTrack(mWidgetPosition)
    }
}
