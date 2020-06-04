package com.tokopedia.talk.feature.reply.analytics

import com.tokopedia.talk.common.analytics.TalkEventTracking
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.track.TrackApp

object TalkReplyTracking {

    private fun eventTalkReply(action: String, label: String, userId: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TalkEventTracking(
                TalkTrackingConstants.EVENT_CATEGORY_TALK, action, label, userId, productId
        ).dataTracking)
    }

    fun eventSendAnswer(userId: String, productId: String, talkId: String) {
        with(TalkReplyTrackingConstants) {
            eventTalkReply(EVENT_ACTION_CLICK_SEND, String.format(EVENT_LABEL_CLICK_SEND, talkId), userId, productId)
        }
    }

    fun sendScreen(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }
}