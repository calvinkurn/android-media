package com.tokopedia.notifications.common

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.notifications.settings.NotificationGeneralPromptBottomSheet
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface

class NotificationReminderPromptGtmTracker constructor(
    private val userSession: UserSessionInterface,
    private val context: Context,
    private val useCase: String
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    private val sharedPreference: SharedPreferences by lazy {
        context.getSharedPreferences(
            NotificationSettingsGtmEvents.TRACKER_PREF_NAME,
            Context.MODE_PRIVATE
        )
    }
    private var frequencyReminderPrompt: Int = 0
    private var eventCategory = CMConstant.GtmTrackerEvents.VALUE_REMINDER_CATEGORY
    private var trackerId = CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_REMINDER_VIEW

    fun sendReminderPromptImpressionEvent(context: Context, pagePath: String) {
        updateReminderPromptFrequency()
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_VIEW_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            trackerId,
            context,
            pagePath
        )
    }

    fun sendReminderPromptClickCtaEvent(context: Context, pagePath: String) {
        trackerId = CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CTA_REMINDER
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_CLICK_CTA,
            trackerId,
            context,
            pagePath
        )
    }

    fun sendReminderPromptClickCloseEvent(context: Context, pagePath: String) {
        trackerId = CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CLOSE_REMINDER
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_CLICK_CLOSE,
            trackerId,
            context,
            pagePath
        )
    }

    private fun updateReminderPromptFrequency() {
        frequencyReminderPrompt = sharedPreference.getInt(NotificationSettingsGtmEvents.FREQ_KEY_REMINDER_PROMPT, 0)
        val editor = sharedPreference.edit()
        editor.putInt(NotificationSettingsGtmEvents.FREQ_KEY_REMINDER_PROMPT, frequencyReminderPrompt + 1)
        editor.apply()
    }

    private fun createMapAndSendEvent(
        event: String,
        eventAction: String,
        trackerId: String,
        context: Context,
        pagePath: String = "",
        pageType: String = "",
        page: String = ""
    ) {
        val userId = if (userSession.userId.isEmpty() || userSession.userId.isBlank()) {
            NotificationSettingsGtmEvents.ZERO
        } else {
            userSession.userId
        }
        val frequency = sharedPreference.getInt(NotificationSettingsGtmEvents.FREQ_KEY_REMINDER_PROMPT, 0)

        val adsId = DeviceInfo.getAdsId(context)
        val useCase = getUseCaseValue()
        val eventLabel =
            "$frequency - $userId - $adsId - ${IrisSession(context).getSessionId()} - $page - $useCase"
        val map = TrackAppUtils.gtmData(
            event,
            eventCategory,
            eventAction,
            eventLabel
        )
        map[CMConstant.GtmTrackerEvents.KEY_DEVICE_ID] = adsId
        map[CMConstant.GtmTrackerEvents.KEY_TRACKER_ID] = trackerId
        map[CMConstant.GtmTrackerEvents.KEY_BUSINESS_UNIT] = CMConstant.GtmTrackerEvents.VALUE_BUSINESS_UNIT
        map[CMConstant.GtmTrackerEvents.KEY_CURRENT_SITE] = CMConstant.GtmTrackerEvents.VALUE_CURRENT_SITE
        map[CMConstant.GtmTrackerEvents.KEY_USER_ID] = userId
        if (pagePath.isNotEmpty()) {
            map[CMConstant.GtmTrackerEvents.KEY_PAGE_PATH] = pagePath
        }
        if (pageType.isNotEmpty()) {
            map[CMConstant.GtmTrackerEvents.KEY_PAGE_TYPE] = pageType
        }
        analyticTracker.sendGeneralEvent(map)
    }

    private fun getUseCaseValue(): String {
        return when (useCase) {
            NotificationGeneralPromptBottomSheet.KEJAR_DISKON -> {
                "Kejar Diskon"
            }
            NotificationGeneralPromptBottomSheet.TAP_TAP_KOTAK -> {
                "Tap Tap Kotak"
            }
            NotificationGeneralPromptBottomSheet.LIVE_SHOPPING -> {
                "Live Shopping"
            }
            else -> {
                ""
            }
        }
    }

}
