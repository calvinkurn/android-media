package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.notifications.common.CMConstant.GtmTrackerEvents
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class NotificationSettingsGtmEvents constructor(
    private val userSession: UserSessionInterface
) {
    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendPromptImpressionEvent(context: Context) {
        createMapAndSendEvent(GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            GtmTrackerEvents.VALUE_TRACKER_ID_IMPRESSION, context)
    }

    fun sendActionNotAllowEvent(context: Context) {
        createMapAndSendEvent(GtmTrackerEvents.VALUE_ACTION_NOT_ALLOW,
            GtmTrackerEvents.VALUE_TRACKER_ID_NOT_ALLOW, context)
    }

    fun sendActionAllowEvent(context: Context) {
        createMapAndSendEvent(GtmTrackerEvents.VALUE_ACTION_ALLOW,
            GtmTrackerEvents.VALUE_TRACKER_ID_ALLOW, context)
    }

    private fun createMapAndSendEvent(eventAction: String, trackerId: String, context: Context) {
        val userId = if (userSession.userId.isEmpty() || userSession.userId.isBlank()) {
            "0"
        } else {
            userSession.userId
        }
        val label = "$userId - ${userSession.adsId} - ${IrisSession(context).getSessionId()}"
        val map = TrackAppUtils.gtmData(
            GtmTrackerEvents.VALUE_EVENT,
            GtmTrackerEvents.VALUE_CATEGORY,
            eventAction,
            label
        )
        map[GtmTrackerEvents.KEY_TRACKER_ID] = trackerId
        map[GtmTrackerEvents.KEY_BUSINESS_UNIT] = GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[GtmTrackerEvents.KEY_CURRENT_SITE] = GtmTrackerEvents.VALUE_CURRENT_SITE
        map[GtmTrackerEvents.KEY_USER_ID] = userId
        analyticTracker.sendGeneralEvent(map)
    }
}