package com.tokopedia.play.broadcaster.analytic.interactive

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by mzennis on 22/07/21.
 */
class PlayBroadcastInteractiveAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroadcastInteractiveAnalytic {

    override fun onImpressInteractiveTool(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "impression on engagement button",
                KEY_EVENT_LABEL to "$channelId - live",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickInteractiveTool(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click engagement button",
                KEY_EVENT_LABEL to "$channelId - live",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onStartInteractive(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String,
        durationInMs: Long
    ) {
        val durationInSecond = TimeUnit.MILLISECONDS.toSeconds(durationInMs)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click terapkan & mulai button",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle - $durationInSecond",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onImpressWinnerIcon(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "impression on winner list",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickWinnerIcon(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click winner list",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }

    override fun onClickChatWinnerIcon(
        channelId: String,
        interactiveId: String,
        interactiveTitle: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_EVENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_ACTION to "click chat in bottom sheet",
                KEY_EVENT_LABEL to "$channelId - live - $interactiveId - $interactiveTitle",
                KEY_CURRENT_SITE to currentSite,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_SHOP_ID to userSession.shopId,
                KEY_USER_ID to userSession.userId
            )
        )
    }
}