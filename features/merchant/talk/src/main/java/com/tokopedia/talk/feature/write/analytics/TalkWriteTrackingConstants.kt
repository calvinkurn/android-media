package com.tokopedia.talk.feature.write.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkWriteTrackingConstants {
    const val EVENT_ACTION_SEND = "${TalkTrackingConstants.EVENT_ACTION_CLICK} kirim new question"
    const val EVENT_CATEGORY_WRITE = "talk question form"
    const val EVENT_ACTION_CLICK_CATEGORY = "${TalkTrackingConstants.EVENT_ACTION_CLICK} category on write questions"
    const val EVENT_LABEL_CLICK_CATEGORY = "talk category:%s; text output:%s;"
    const val EVENT_LABEL_CLICK_SEND = "talk category:%s; success:%s; error message:%s;"
    const val SEND_SUCCESS = "success"
    const val SEND_FAIL = "failed"
    const val EVENT_ACTION_CLICK_CHAT_SELLER = "${TalkTrackingConstants.EVENT_ACTION_CLICK} chat penjual on talk question form"

    const val SCREEN_NAME_WRITE = "/talk-question-form"
}