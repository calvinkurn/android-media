package com.tokopedia.notifications.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.notifications.common.CMConstant.GtmTrackerEvents
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface


class NotificationSettingsGtmEvents constructor(
    private val userSession: UserSessionInterface,
    private val context: Context
) {
    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm
    private val sharedPreference: SharedPreferences by lazy {
        context.getSharedPreferences(TRACKER_PREF_NAME, MODE_PRIVATE)
    }
    private var trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_VIEW
    private var trackerIdAllow = GtmTrackerEvents.VALUE_TRACKER_ID_ALLOW
    private var trackerIdNotAllow = GtmTrackerEvents.VALUE_TRACKER_ID_NOT_ALLOW
    private var eventCategory = GtmTrackerEvents.VALUE_CATEGORY
    private var frequency: Int = 0

    fun updateFrequency() {
        val editor = sharedPreference.edit()
        editor.putInt(FREQ_KEY, frequency + 1)
        editor.apply()
    }


    fun sendPromptImpressionEvent(context: Context) {
        if (GlobalConfig.isSellerApp()) {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_VIEW_SA
            trackerIdAllow = GtmTrackerEvents.VALUE_TRACKER_ID_ALLOW_SA
            trackerIdNotAllow = GtmTrackerEvents.VALUE_TRACKER_ID_NOT_ALLOW_SA
            eventCategory = GtmTrackerEvents.VALUE_CATEGORY_SA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_VIEW_CONTENT, GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            trackerIdView, context
        )
    }

    fun sendActionNotAllowEvent(context: Context) {
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT, GtmTrackerEvents.VALUE_ACTION_NOT_ALLOW,
            trackerIdNotAllow, context
        )
    }

    fun sendActionAllowEvent(context: Context) {
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT, GtmTrackerEvents.VALUE_ACTION_ALLOW,
            trackerIdAllow, context
        )
    }

    private fun createMapAndSendEvent(
        event: String,
        eventAction: String,
        trackerId: String,
        context: Context
    ) {
        val userId = if (userSession.userId.isEmpty() || userSession.userId.isBlank()) {
            ZERO
        } else {
            userSession.userId
        }
        frequency = sharedPreference.getInt(FREQ_KEY, 0)
        val eventLabel =
            "$frequency - $userId - ${userSession.adsId} - ${IrisSession(context).getSessionId()}"
        val map = TrackAppUtils.gtmData(
            event,
            eventCategory,
            eventAction,
            eventLabel
        )
        map[GtmTrackerEvents.KEY_DEVICE_ID] = userSession.adsId
        map[GtmTrackerEvents.KEY_TRACKER_ID] = trackerId
        map[GtmTrackerEvents.KEY_BUSINESS_UNIT] = GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[GtmTrackerEvents.KEY_CURRENT_SITE] = GtmTrackerEvents.VALUE_CURRENT_SITE
        map[GtmTrackerEvents.KEY_USER_ID] = userId
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val TRACKER_PREF_NAME = "NotificationSettingsTracker"
        const val FREQ_KEY = "frequency"
        const val ZERO = "0"
    }
}