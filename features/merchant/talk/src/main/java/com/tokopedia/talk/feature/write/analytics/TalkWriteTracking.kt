package com.tokopedia.talk.feature.write.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.track.TrackApp

object TalkWriteTracking {

    private fun eventTalkWriting(userId: String, productId: String, action: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                with(TalkTrackingConstants) {
                    mapOf(
                            TRACKING_EVENT to EVENT_TALK,
                            TRACKING_EVENT_CATEGORY to TalkWriteTrackingConstants.EVENT_CATEGORY_WRITE,
                            TRACKING_EVENT_ACTION to action,
                            TRACKING_EVENT_LABEL to "",
                            TRACKING_SCREEN_NAME to TalkWriteTrackingConstants.SCREEN_NAME_WRITE,
                            TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                            TRACKING_USER_ID to userId,
                            TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK,
                            TRACKING_PRODUCT_ID to productId
                    )
                }
        )
    }

    fun eventClickSendButton(userId: String, productId: String) {
        eventTalkWriting(userId, productId, TalkWriteTrackingConstants.EVENT_ACTION_SEND)
    }

    fun eventClickChips(userId: String, productId: String) {
        eventTalkWriting(userId, productId, TalkWriteTrackingConstants.EVENT_ACTION_CLICK_CATEGORY)
    }

    fun eventClickAskSeller() {

    }


}