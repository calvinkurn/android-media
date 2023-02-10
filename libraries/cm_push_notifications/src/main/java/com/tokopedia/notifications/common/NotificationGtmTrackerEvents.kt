package com.tokopedia.notifications.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
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
    private var frequencyNativePrompt: Int = 0
    private var frequencyGeneralPrompt: Int = 0
    private var isNativePrompt = false

    fun sendPromptImpressionEvent(context: Context) {
        isNativePrompt = true
        updateNativePromptFrequency()
        if (GlobalConfig.isSellerApp()) {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_VIEW_SA
            eventCategory = GtmTrackerEvents.VALUE_CATEGORY_SA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_VIEW_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            trackerIdView,
            context
        )
    }

    private fun updateNativePromptFrequency() {
        frequencyNativePrompt = sharedPreference.getInt(FREQ_KEY_NATIVE_PROMPT, 0)
        val editor = sharedPreference.edit()
        editor.putInt(FREQ_KEY_NATIVE_PROMPT, frequencyNativePrompt + 1)
        editor.apply()
    }

    fun sendActionNotAllowEvent(context: Context) {
        isNativePrompt = true
        if (GlobalConfig.isSellerApp()) {
            eventCategory = GtmTrackerEvents.VALUE_CATEGORY_SA
            trackerIdNotAllow = GtmTrackerEvents.VALUE_TRACKER_ID_NOT_ALLOW_SA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_NOT_ALLOW,
            trackerIdNotAllow,
            context
        )
    }

    fun sendActionAllowEvent(context: Context) {
        isNativePrompt = true
        if (GlobalConfig.isSellerApp()) {
            eventCategory = GtmTrackerEvents.VALUE_CATEGORY_SA
            trackerIdAllow = GtmTrackerEvents.VALUE_TRACKER_ID_ALLOW_SA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_ALLOW,
            trackerIdAllow,
            context
        )
    }

    fun sendGeneralPromptImpressionEvent(context: Context, pagePath: String) {
        isNativePrompt = false
        updateGeneralPromptFrequency()
        eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY
        if (GlobalConfig.isSellerApp()) {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_VIEW_GEN_SA
            eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY_SA
        } else {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_VIEW_GEN_MA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_VIEW_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            trackerIdView,
            context,
            pagePath
        )
    }

    private fun updateGeneralPromptFrequency() {
        frequencyGeneralPrompt = sharedPreference.getInt(FREQ_KEY_GENERAL_PROMPT, 0)
        val editor = sharedPreference.edit()
        editor.putInt(FREQ_KEY_GENERAL_PROMPT, frequencyGeneralPrompt + 1)
        editor.apply()
    }

    fun sendGeneralPromptClickCloseEvent(context: Context, pagePath: String) {
        isNativePrompt = false
        eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY
        if (GlobalConfig.isSellerApp()) {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CLOSE_GEN_SA
            eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY_SA
        } else {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CLOSE_GEN_MA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_CLICK_CLOSE,
            trackerIdView,
            context,
            pagePath
        )
    }

    fun sendGeneralPromptClickCtaEvent(context: Context, pagePath: String) {
        isNativePrompt = false
        eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY
        if (GlobalConfig.isSellerApp()) {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CTA_GEN_SA
            eventCategory = GtmTrackerEvents.VALUE_GEN_CATEGORY_SA
        } else {
            trackerIdView = GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CTA_GEN_MA
        }
        createMapAndSendEvent(
            GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            GtmTrackerEvents.VALUE_ACTION_CLICK_CTA,
            trackerIdView,
            context,
            pagePath
        )
    }

    private fun createMapAndSendEvent(
        event: String,
        eventAction: String,
        trackerId: String,
        context: Context,
        pagePath: String = ""
    ) {
        val userId = if (userSession.userId.isEmpty() || userSession.userId.isBlank()) {
            ZERO
        } else {
            userSession.userId
        }
        val frequency = if (isNativePrompt) {
            sharedPreference.getInt(FREQ_KEY_NATIVE_PROMPT, 0)
        } else {
            sharedPreference.getInt(FREQ_KEY_GENERAL_PROMPT, 0)
        }

        val adsId = DeviceInfo.getAdsId(context)
        val eventLabel =
            "$frequency - $userId - adsId - ${IrisSession(context).getSessionId()}"
        val map = TrackAppUtils.gtmData(
            event,
            eventCategory,
            eventAction,
            eventLabel
        )
        map[GtmTrackerEvents.KEY_DEVICE_ID] = adsId
        map[GtmTrackerEvents.KEY_TRACKER_ID] = trackerId
        map[GtmTrackerEvents.KEY_BUSINESS_UNIT] = GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[GtmTrackerEvents.KEY_CURRENT_SITE] = GtmTrackerEvents.VALUE_CURRENT_SITE
        map[GtmTrackerEvents.KEY_USER_ID] = userId
        if (pagePath.isNotEmpty()) {
            map[GtmTrackerEvents.KEY_PAGE_PATH] = pagePath
        }
        analyticTracker.sendGeneralEvent(map)
    }

    companion object {
        const val TRACKER_PREF_NAME = "NotificationSettingsTracker"
        const val FREQ_KEY_NATIVE_PROMPT = "frequencyNativePrompt"
        const val FREQ_KEY_GENERAL_PROMPT = "frequencyGeneralPrompt"
        const val ZERO = "0"
    }
}
