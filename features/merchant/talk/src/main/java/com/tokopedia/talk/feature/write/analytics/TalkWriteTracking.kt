package com.tokopedia.talk.feature.write.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.track.TrackApp

object TalkWriteTracking {

    private fun eventTalkWriting(userId: String, productId: String, action: String, label: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                with(TalkTrackingConstants) {
                    mapOf(
                            TRACKING_EVENT to EVENT_TALK,
                            TRACKING_EVENT_CATEGORY to TalkWriteTrackingConstants.EVENT_CATEGORY_WRITE,
                            TRACKING_EVENT_ACTION to action,
                            TRACKING_EVENT_LABEL to label,
                            TRACKING_SCREEN_NAME to TalkWriteTrackingConstants.SCREEN_NAME_WRITE,
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK,
                            TRACKING_PRODUCT_ID to productId
                    )
                }
        )
    }

    fun eventClickSendButton(userId: String, productId: String, category: String, isSuccess: Boolean, errorMessage: String? = null, isVariantSelected: Boolean) {
        with(TalkWriteTrackingConstants) {
            val successOrFailTag = if(isSuccess) {SEND_SUCCESS} else {SEND_FAIL}
            val eventLabel = String.format(EVENT_LABEL_CLICK_SEND, category, successOrFailTag, errorMessage.toString(), isVariantSelected.toString())
            eventTalkWriting(userId, productId, EVENT_ACTION_SEND, eventLabel)
        }

    }

    fun eventClickChips(userId: String, productId: String, category: String, message: String, isVariantSelected: Boolean) {
        with(TalkWriteTrackingConstants) {
            eventTalkWriting(userId, productId,
                    EVENT_ACTION_CLICK_CATEGORY,
                    String.format(EVENT_LABEL_CLICK_CATEGORY, category, message, isVariantSelected.toString()))
        }
    }

}