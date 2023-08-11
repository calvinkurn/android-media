package com.tokopedia.play.broadcaster.analytic.setup.schedule

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_VIEW_EVENT
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
class PlayBroScheduleAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroScheduleAnalytic {

    private val shopId: String
        get() = userSession.shopId

    override fun viewDialogConfirmDeleteSchedule() {
        sendEvent(
            event = KEY_TRACK_VIEW_EVENT,
            eventAction = "view pop up delete confirmation",
            eventLabel = ""
        )
    }

    override fun clickStartLiveBeforeScheduleTime() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "click mulai live streaming",
            eventLabel = ""
        )
    }

    /**
     * {"event":"clickPG","eventAction":"click - atur jadwal","eventCategory":"seller broadcast","eventLabel":"{shop_id}","businessUnit":"play","currentSite":"tokopediamarketplace","sessionIris":"{session_iris}","userId":"{user_id}"}
     */
    override fun clickSetSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "click - atur jadwal",
            eventLabel = shopId
        )
    }

    override fun clickSaveSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "click save content scheduling",
            eventLabel = shopId
        )
    }

    override fun clickDeleteSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "click - delete content scheduled",
            eventLabel = shopId
        )
    }

    override fun clickCloseSetupSchedule() {
        sendEvent(
            event = KEY_TRACK_CLICK_EVENT,
            eventAction = "click - x content scheduling",
            eventLabel = shopId
        )
    }

    private fun sendEvent(
        event: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to event,
                Key.eventAction to eventAction,
                Key.eventCategory to KEY_TRACK_CATEGORY,
                Key.eventLabel to eventLabel,
                Key.currentSite to currentSite,
                Key.userId to userSession.userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }
}
