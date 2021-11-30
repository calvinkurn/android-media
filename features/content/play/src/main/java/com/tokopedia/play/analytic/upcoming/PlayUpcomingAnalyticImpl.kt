package com.tokopedia.play.analytic.upcoming

import com.tokopedia.play.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayUpcomingAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
): PlayUpcomingAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun impressUpcomingPage(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_UPCOMING_IRIS,
                KEY_EVENT_ACTION to "impression on upcoming page",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    override fun clickRemindMe(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_UPCOMING,
                KEY_EVENT_ACTION to "click on ingatkan saya",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    override fun clickWatchNow(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_UPCOMING,
                KEY_EVENT_ACTION to "click on tonton sekarang",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }
}