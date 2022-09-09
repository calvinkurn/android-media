package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.notifications.common.CMConstant.GtmTrackerEvents
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession


class NotificationSettingsGtmEvents constructor(
    private val userSession: UserSession
) {
    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendActionNotAllowEvent(context: Context) {
        val label = "${userSession.userId} - ${IrisSession(context).getSessionId()}"
        val map = TrackAppUtils.gtmData(
            GtmTrackerEvents.VALUE_EVENT,
            GtmTrackerEvents.VALUE_CATEGORY,
            GtmTrackerEvents.VALUE_ACTION_NOT_ALLOW,
            label
        )
        map[GtmTrackerEvents.KEY_TRACKER_ID] = GtmTrackerEvents.VALUE_TRACKER_ID_NOT_ALLOW
        map[GtmTrackerEvents.KEY_BUSINESS_UNIT] = GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[GtmTrackerEvents.KEY_CURRENT_SITE] = GtmTrackerEvents.VALUE_CURRENT_SITE
        map[GtmTrackerEvents.KEY_USER_ID] = userSession.userId
        analyticTracker.sendGeneralEvent(map)
    }

    fun sendActionAllowEvent(context: Context) {
        val label = "${userSession.userId} - ${IrisSession(context).getSessionId()}"
        val map = TrackAppUtils.gtmData(
            GtmTrackerEvents.VALUE_EVENT,
            GtmTrackerEvents.VALUE_CATEGORY,
            GtmTrackerEvents.VALUE_ACTION_ALLOW,
            label
        )
        map[GtmTrackerEvents.KEY_TRACKER_ID] = GtmTrackerEvents.VALUE_TRACKER_ID_ALLOW
        map[GtmTrackerEvents.KEY_BUSINESS_UNIT] = GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[GtmTrackerEvents.KEY_CURRENT_SITE] = GtmTrackerEvents.VALUE_CURRENT_SITE
        map[GtmTrackerEvents.KEY_USER_ID] = userSession.userId
        analyticTracker.sendGeneralEvent(map)
    }

}