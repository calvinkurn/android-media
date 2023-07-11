package com.tokopedia.notifications.common

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.config.GlobalConfig
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
    private var eventCategory = if (GlobalConfig.isSellerApp()) {
        CMConstant.GtmTrackerEvents.VALUE_REMINDER_CATEGORY_SA
    } else {
        CMConstant.GtmTrackerEvents.VALUE_REMINDER_CATEGORY
    }
    private var trackerId = CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_REMINDER_VIEW
    private var pageType = ""
    private val frequencyKey = useCase + userSession.userId

    fun sendReminderPromptImpressionEvent(context: Context, pagePath: String) {
        if (GlobalConfig.isSellerApp()) {
            trackerId = CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_REMINDER_VIEW_SA
        }

        updateReminderPromptFrequency(frequencyKey)
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_VIEW_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_IMPRESSION,
            trackerId,
            context,
            pagePath
        )
    }

    fun sendReminderPromptClickCtaEvent(context: Context, pagePath: String) {
        trackerId = if (GlobalConfig.isSellerApp()) {
            CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CTA_REMINDER_SA
        } else {
            CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CTA_REMINDER
        }
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_CLICK_CTA,
            trackerId,
            context,
            pagePath
        )
    }

    fun sendReminderPromptClickCloseEvent(context: Context, pagePath: String) {
        trackerId = if (GlobalConfig.isSellerApp()) {
            CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CLOSE_REMINDER_SA
        } else {
            CMConstant.GtmTrackerEvents.VALUE_TRACKER_ID_CLICK_CLOSE_REMINDER
        }
        createMapAndSendEvent(
            CMConstant.GtmTrackerEvents.VALUE_EVENT_CLICK_CONTENT,
            CMConstant.GtmTrackerEvents.VALUE_ACTION_CLICK_CLOSE,
            trackerId,
            context,
            pagePath
        )
    }

    private fun updateReminderPromptFrequency(frequencyKey: String) {
        val frequency = sharedPreference.getInt(frequencyKey, 0)
        val editor = sharedPreference.edit()
        editor.putInt(frequencyKey, frequency + 1)
        editor.apply()
    }

    private fun createMapAndSendEvent(
        event: String,
        eventAction: String,
        trackerId: String,
        context: Context,
        pagePath: String = ""
    ) {
        val userId = if (userSession.userId.isEmpty() || userSession.userId.isBlank()) {
            NotificationSettingsGtmEvents.ZERO
        } else {
            userSession.userId
        }
        val frequency = sharedPreference.getInt(frequencyKey, 0)

        val adsId = DeviceInfo.getAdsId(context)
        val useCase = getUseCaseValue()
        val page = getPageName(pagePath)
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
        when (useCase) {
            NotificationGeneralPromptBottomSheet.KEJAR_DISKON -> {
                pageType = "general"
                return KEJAR_DISKON
            }
            NotificationGeneralPromptBottomSheet.TAP_TAP_KOTAK -> {
                pageType = "view gift box page"
                return TAP_TAP_KOTAK
            }
            NotificationGeneralPromptBottomSheet.LIVE_SHOPPING -> {
                pageType = "upcoming page"
                return LIVE_SHOPPING
            }
            NotificationGeneralPromptBottomSheet.STOCK_REMINDER -> {
                pageType = "product list page"
                return STOCK_REMINDER
            }
            else -> {
                return ""
            }
        }
    }

    private fun getPageName(pagePath: String): String {
        return when (pagePath) {
            PLAY_ACTIVITY -> {
                LIVE_SHOPPING_PAGE
            }
            GIFT_BOX_DAILY_ACTIVITY -> {
                GIFT_BOX_DAILY_PAGE
            }
            DISCOVERY_ACTIVITY -> {
                DISCOVERY_PAGE
            }
            MAIN_PARENT_ACTIVITY -> {
                HOME_PAGE
            }
            PRODUCT_MANAGE_ACTIVITY -> {
                PRODUCT_LIST_PAGE
            }
            else -> {
                pagePath
            }
        }
    }

    companion object {
        const val KEJAR_DISKON = "kejar Diskon"
        const val TAP_TAP_KOTAK = "Tap Tap Kotak"
        const val LIVE_SHOPPING = "Live Shopping"
        const val STOCK_REMINDER = "Stock Reminder"
        const val DISCOVERY_ACTIVITY = "DiscoveryActivity"
        const val PLAY_ACTIVITY = "PlayActivity"
        const val GIFT_BOX_DAILY_ACTIVITY = "GiftBoxDailyActivity"
        const val MAIN_PARENT_ACTIVITY = "MainParentActivity"
        const val PRODUCT_MANAGE_ACTIVITY = "ProductManageActivity"
        const val DISCOVERY_PAGE = "Discovery Page"
        const val LIVE_SHOPPING_PAGE = "Live Shopping Page"
        const val GIFT_BOX_DAILY_PAGE = "GiftBoxDaily Page"
        const val HOME_PAGE = "Home Page"
        const val PRODUCT_LIST_PAGE = "Product List Page"
    }
}
