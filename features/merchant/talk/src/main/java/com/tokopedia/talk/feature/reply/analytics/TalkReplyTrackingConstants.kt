package com.tokopedia.talk.feature.reply.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkReplyTrackingConstants {
    const val EVENT_ACTION_CLICK_SEND = "${TalkTrackingConstants.EVENT_ACTION_CLICK} kirim to reply talk"
    const val EVENT_LABEL_CLICK_SEND = "%s - pagesource:%s;"
    const val REPLY_SCREEN_NAME = "/talkdetail"
    const val IS_LOGGED_IN = "true"
    const val EVENT_PRODUCT_CLICK = "productClick"
    const val EVENT_PRODUCT_VIEW  = "productView"
    const val EVENT_ACTION_CLICK_PRODUCT_CARD = "click - product card on inbox talk"
    const val EVENT_ACTION_VIEW_PRODUCT_CARD = "view - product card on inbox talk"
    const val INBOX_SCREEN_NAME = "/inbox - talk"
    const val INBOX_PAGE = "inbox"
    const val PRODUCT_PAGE = "pdp"
}