package com.tokopedia.chatbot.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
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
        const val EVENT_CATEGORY_VIDEO_PICK = "chatbot select video"
        const val EVENT_ACTION_VIDEO_PICK = "click - select video"
        const val EVENT_LABEL_VIDEO_PICK = "{select media}"
        const val KEY_TRACKER_ID = "trackerId"
        const val TRACKER_ID_VIDEO_PICK = "32055"
        const val TRACKER_ID_VIDEO_UPLOAD = "32956"
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

    fun eventOnVideoPick() {
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            EVENT_CATEGORY_VIDEO_PICK,
            EVENT_ACTION_VIDEO_PICK,
            EVENT_LABEL_VIDEO_PICK
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_VIDEO_PICK
        sendGeneralEventWithTrackerId(map)
    }

    fun eventOnVideoUpload(videoFilePath: String, extension: String, videoSize: String) {
        val eventLabel =  "media name:$videoFilePath ;media type:$extension ;media size:$videoSize "
        val map = TrackAppUtils.gtmData(
            EVENT_NAME,
            EVENT_CATEGORY_VIDEO_PICK,
            EVENT_ACTION_VIDEO_PICK,
            eventLabel
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_VIDEO_UPLOAD
        sendGeneralEventWithTrackerId(map)
    }

    private fun sendGeneralEventWithTrackerId(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_CX
        map[KEY_CURRENT_SITE] = CURRENT_SITE_CX
        getTracker().sendGeneralEvent(map)
    }

    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.userId
        map[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_CX
        map[KEY_CURRENT_SITE] = CURRENT_SITE_CX
        getTracker().sendGeneralEvent(map)
    }

}
