package com.tokopedia.talk.feature.write.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkWriteTrackingConstants {
    const val EVENT_ACTION_SEND = "${TalkTrackingConstants.EVENT_ACTION_CLICK} kirim to create new talk"
    const val EVENT_CATEGORY_WRITE = "talk question form"
    const val EVENT_ACTION_CLICK_CATEGORY = "${TalkTrackingConstants.EVENT_ACTION_CLICK} category on write questions"
    const val SCREEN_NAME_WRITE = "/talk-question-form"
}