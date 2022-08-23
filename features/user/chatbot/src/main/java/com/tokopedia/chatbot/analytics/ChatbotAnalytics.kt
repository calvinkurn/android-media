package com.tokopedia.chatbot.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val KEY_EVENT = "event"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val EVENT_VALUE_CLICK = "clickChatbot"
private const val EVENT_VALUE_SHOW = "viewChatbotIris"
private const val EVENT_CATEGORY_VALUE = "contact us v3"

class ChatbotAnalytics @Inject constructor(
    private val userSession: UserSessionInterface
) {

    companion object {
        const val EVENT_NAME = "clickCX"
        const val EVENT_ACTION_CLOSE_SHEET = "click close inbox migration bottomsheet"
        const val EVENT_ACTION_CLICK_TOKOPEDIA_CATE = "click check tokopedia care"
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT_CX = "Customer Excellence"
        const val CURRENT_SITE_CX = "tokopediamarketplace"
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun eventClick(eventAction: String, eventLabel: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_VALUE_CLICK,
                KEY_EVENT_CATEGORY to EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventShowView(eventAction: String, eventLabel: String = "") {
        val map = mapOf(
                KEY_EVENT to EVENT_VALUE_SHOW,
                KEY_EVENT_CATEGORY to EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventOnClickCancelBottomSheet() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            EVENT_CATEGORY_VALUE,
            EVENT_ACTION_CLOSE_SHEET,
            ""
        )
        sendGeneralEvent(map)

    }

    fun eventOnClickTokopediaCare() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            EVENT_CATEGORY_VALUE,
            EVENT_ACTION_CLICK_TOKOPEDIA_CATE,
            ""
        )
        sendGeneralEvent(map)

    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.userId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_CX
        map[KEY_CURRENT_SITE] = CURRENT_SITE_CX
        getTracker().sendGeneralEvent(map)
    }

}