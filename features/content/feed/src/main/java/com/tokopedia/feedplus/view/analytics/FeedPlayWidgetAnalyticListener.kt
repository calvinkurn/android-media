package com.tokopedia.feedplus.view.analytics

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by jegul on 19/10/20
 */
class FeedPlayWidgetAnalyticListener(
        private val trackingQueue: TrackingQueue,
        private val userSession: UserSessionInterface
) : PlayWidgetAnalyticListener {

    private val userId: String
        get() = userSession.userId

    private var widgetPosition = RecyclerView.NO_POSITION

    override fun onImpressPlayWidget(view: PlayWidgetView, widgetPositionInList: Int) {
        widgetPosition = widgetPositionInList
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

    override fun onClickChannelCard(view: PlayWidgetSmallView, channelPositionInList: Int) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
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

    override fun onImpressChannelCard(view: PlayWidgetSmallView, channelPositionInList: Int) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
    }

    private fun withWidgetPosition(onTrack: (Int) -> Unit) {
        if (widgetPosition == RecyclerView.NO_POSITION) return
        onTrack(widgetPosition)
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