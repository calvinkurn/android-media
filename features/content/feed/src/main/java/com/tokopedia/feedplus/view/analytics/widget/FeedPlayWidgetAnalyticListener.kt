package com.tokopedia.feedplus.view.analytics.widget

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.HashMap
import javax.inject.Inject

/**
 * Created by jegul on 19/10/20
 */
class FeedPlayWidgetAnalyticListener @Inject constructor(
        private val trackingQueue: TrackingQueue,
        private val userSession: UserSessionInterface
) : PlayWidgetAnalyticListener {

    private val userId: String
        get() = userSession.userId

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(view: PlayWidgetView, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList

        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "viewFeedIris",
                        KEY_EVENT_CATEGORY to "feed updates - cmp",
                        KEY_EVENT_ACTION to "impression on play sgc banner",
                        KEY_EVENT_LABEL to widgetPositionInList,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    override fun onClickViewAll(view: PlayWidgetSmallView) = withWidgetPosition { pos ->

        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "clickFeed",
                        KEY_EVENT_CATEGORY to "feed updates - cmp",
                        KEY_EVENT_ACTION to "click view all",
                        KEY_EVENT_LABEL to "0 - Tokopedia Play - $pos",
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    override fun onClickChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = "promoClick",
                eventCategory = "feed updates - cmp",
                eventAction = "click",
                eventLabel = "click channel - ${item.channelType.toTrackingType()} - ${item.channelId} - $channelPositionInList - $pos - is autoplay $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = item.channelId,
                                name = "feed - play sgc channel - $pos",
                                creative = item.title,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId)
                .appendBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
                .appendCurrentSite(KEY_TRACK_CURRENT_SITE)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickBannerCard(view: PlayWidgetSmallView) = withWidgetPosition { pos ->
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to "clickFeed",
                        KEY_EVENT_CATEGORY to "feed updates - cmp",
                        KEY_EVENT_ACTION to "click other content",
                        KEY_EVENT_LABEL to pos,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId
                )
        )
    }

    override fun onImpressChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) = withWidgetPosition { pos ->
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = "promoView",
                eventCategory = "feed updates - cmp",
                eventAction = "impression on play sgc channel",
                eventLabel = "${item.channelType.toTrackingType()} - ${item.channelId} - $channelPositionInList - $pos - is autoplay $isAutoPlay",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = item.channelId,
                                name = "feed - play sgc channel - $pos",
                                creative = item.title,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId)
                .appendBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
                .appendCurrentSite(KEY_TRACK_CURRENT_SITE)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    private fun withWidgetPosition(onTrack: (Int) -> Unit) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
        onTrack(widgetPosition)
    }

    private fun PlayWidgetChannelType.toTrackingType() = when (this) {
        PlayWidgetChannelType.Live -> "live"
        PlayWidgetChannelType.Vod -> "vod"
        PlayWidgetChannelType.Upcoming -> "upcoming"
        else -> "null"
    }

    companion object {

        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_USER_ID = "userId"

        private const val KEY_TRACK_BUSINESS_UNIT = "play"
        private const val KEY_TRACK_CURRENT_SITE = "tokopediamarketplace"
    }
}