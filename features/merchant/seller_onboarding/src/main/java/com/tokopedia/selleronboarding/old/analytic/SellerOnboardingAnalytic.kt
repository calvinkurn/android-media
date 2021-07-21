package com.tokopedia.selleronboarding.old.analytic

import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 16/04/20
 */

/**
 * Seller Onboarding Tracker
 * Data Layer : https://docs.google.com/spreadsheets/d/1AZjuQ_dg25EvEEWmE8MPMo0f1_DT4IyZPaNpt4cxidA/edit#gid=868838969
 * */
object SellerOnboardingAnalytic {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_SCREEN_NAME = "screenName"
    private const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_CLIENT_ID = "clientId"
    private const val KEY_SESSION_IRIS = "sessionIris"
    private const val KEY_USER_ID = "userId"
    private const val KEY_BUSINESS_UNIT = "businessUnit"

    private const val SCREEN = "screen"
    private const val OPEN_SCREEN = "openScreen"
    private const val EVENT_CLICK_LOGIN = "clickLogin"
    private const val CLICK_LOGIN = "click login"
    private const val TOKOPEDIA_SELLER = "tokopediaseller"
    private const val ONBOARDING_SELLER = "/onboarding - seller"
    private const val ONBOARDING_SELLER_PAGE = "onboarding seller page"
    private const val PHYSICAL_GOODS = "physical goods"
    private const val VALUE_FALSE = "{false}"

    fun sendEventOpenScreen(slidePosition: Int, irisSessionId: String, clientId: String) {
        val mIrisSessionId = if (irisSessionId.isNotBlank()) "{$irisSessionId}" else ""
        val mClientId = if (clientId.isNotBlank()) "{$clientId}" else ""
        val event = mutableMapOf<String, Any>(
                KEY_EVENT to OPEN_SCREEN,
                KEY_SCREEN_NAME to "$ONBOARDING_SELLER $slidePosition",
                KEY_IS_LOGGED_IN_STATUS to VALUE_FALSE,
                KEY_CLIENT_ID to mClientId,
                KEY_SESSION_IRIS to mIrisSessionId,
                KEY_USER_ID to "",
                KEY_BUSINESS_UNIT to PHYSICAL_GOODS
        )

        sendGeneralEvent(eventMap = event)
    }

    /**
     * @param position start with 0
     * */
    fun sendEventClickOpenApp(position: Int, irisSessionId: String, clientId: String) {
        val mIrisSessionId = if (irisSessionId.isNotBlank()) "{$irisSessionId}" else ""
        val mClientId = if (clientId.isNotBlank()) "{$clientId}" else ""
        val positionStr = "${convertOrdinal(position.plus(1))} $SCREEN"
        val event = createMap(
                event = EVENT_CLICK_LOGIN,
                category = ONBOARDING_SELLER_PAGE,
                action = arrayOf(CLICK_LOGIN, positionStr).joinToString(" - "),
                label = ""
        )
        event[KEY_SCREEN_NAME] = "$ONBOARDING_SELLER ${position.plus(1)}"
        event[KEY_CURRENT_SITE] = TOKOPEDIA_SELLER
        event[KEY_CLIENT_ID] = mClientId
        event[KEY_SESSION_IRIS] = mIrisSessionId
        event[KEY_USER_ID] = ""
        event[KEY_BUSINESS_UNIT] = PHYSICAL_GOODS

        sendGeneralEvent(event)
    }

    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to event,
                KEY_EVENT_CATEGORY to category,
                KEY_EVENT_ACTION to action,
                KEY_EVENT_LABEL to label
        )
    }

    private fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun convertOrdinal(i: Int): String? {
        val sufixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (i % 100) {
            11, 12, 13 -> i.toString() + "th"
            else -> i.toString() + sufixes[i % 10]
        }
    }
}