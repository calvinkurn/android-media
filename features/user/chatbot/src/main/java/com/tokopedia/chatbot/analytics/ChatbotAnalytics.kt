package com.tokopedia.chatbot.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

private const val KEY_EVENT = "event"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val EVENT_VALUE_CLICK = "clickChatbot"
private const val EVENT_VALUE_SHOW = "viewChatbotIris"
private const val EVENT_CATEGORY_VALUE = "contact us v3"

class ChatbotAnalytics {
    companion object {
        val chatbotAnalytics: ChatbotAnalytics by lazy { ChatbotAnalytics() }
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

}