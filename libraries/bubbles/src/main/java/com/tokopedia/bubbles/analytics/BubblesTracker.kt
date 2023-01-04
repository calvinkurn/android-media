package com.tokopedia.bubbles.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object BubblesTracker {

    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_TRACKER_ID = "trackerId"

    private const val EVENT_VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
    private const val EVENT_ACTION_IMPRESSION_NOTIFICATION_BUBBLE_CHAT = "impression on notification bubble chat"
    private const val EVENT_CATEGORY_BUBBLE_CHAT_DETAIL = "bubble chat detail"

    private const val COMMUNICATION = "communication"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val TRACKER_ID_37706 = "37706"

    fun sendImpressionTracking(
        shopId: String,
        messageId: String,
        buyerUserId: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(getGeneralEvent(shopId, messageId, buyerUserId))
    }

    private fun getGeneralEvent(
        shopId: String,
        messageId: String,
        buyerUserId: String
    ): Map<String, Any> {
        val event = TrackAppUtils.gtmData(
            EVENT_VIEW_COMMUNICATION_IRIS,
            EVENT_CATEGORY_BUBBLE_CHAT_DETAIL,
            EVENT_ACTION_IMPRESSION_NOTIFICATION_BUBBLE_CHAT,
            "$shopId - $messageId - $buyerUserId"
        )
        event[KEY_BUSINESS_UNIT] = COMMUNICATION
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[KEY_TRACKER_ID] = TRACKER_ID_37706

        return event
    }

}
