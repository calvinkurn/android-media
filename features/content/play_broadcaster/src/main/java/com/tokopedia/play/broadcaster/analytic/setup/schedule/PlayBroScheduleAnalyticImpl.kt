package com.tokopedia.play.broadcaster.analytic.setup.schedule

import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_VIEW
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_VIEW_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
class PlayBroScheduleAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
): PlayBroScheduleAnalytic {

    private val shopId: String
        get() = userSession.shopId

    override fun viewDialogConfirmDeleteSchedule() {
        sendEvent(
            event = KEY_TRACK_VIEW_EVENT,
            eventAction = "$KEY_TRACK_VIEW pop up delete confirmation",
            eventLabel = "",
        )
    }

    override fun clickStartLiveBeforeScheduleTime() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "$KEY_TRACK_CLICK mulai live streaming",
            eventLabel = "",
        )
    }

    /**
     * {"event":"clickPG","eventAction":"click - atur jadwal","eventCategory":"seller broadcast","eventLabel":"{shop_id}","businessUnit":"play","currentSite":"tokopediamarketplace","sessionIris":"{session_iris}","userId":"{user_id}"}
     */
    override fun clickSetSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "$KEY_TRACK_CLICK - atur jadwal",
            eventLabel = shopId,
        )
    }

    override fun clickSaveSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "$KEY_TRACK_CLICK save content scheduling",
            eventLabel = shopId,
        )
    }

    override fun clickDeleteSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "$KEY_TRACK_CLICK - delete content scheduled",
            eventLabel = shopId,
        )
    }

    override fun clickCloseSetupSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "$KEY_TRACK_CLICK - x content scheduling",
            eventLabel = shopId,
        )
    }

    private fun sendEvent(
        event: String,
        eventAction: String,
        eventLabel: String,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to event,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY,
                KEY_EVENT_LABEL to eventLabel,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
            )
        )
    }
}