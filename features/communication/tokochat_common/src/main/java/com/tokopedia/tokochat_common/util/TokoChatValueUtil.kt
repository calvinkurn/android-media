package com.tokopedia.tokochat_common.util

object TokoChatValueUtil {
    /**
     * Role Value
     */
    const val DRIVER = "driver"
    const val CUSTOMER = "tokopedia_customer"

    /**
     * SOURCE
     */
    const val TOKOFOOD = "tokofood"

    /**
     * Message status
     */
    const val PENDING_VALUE = 0
    const val SENT_VALUE = 1
    const val READ_VALUE = 2
    const val FAILED_VALUE = 3

    /**
     * Date Format
     */
    const val DATE_FORMAT = "d MMM yyyy"
    const val RELATIVE_TODAY = "Hari ini"
    const val RELATIVE_YESTERDAY = "Kemarin"
    const val HEADER_DATE_FORMAT = "d MMMM, yyyy"

    /**
     * Compose & Bubble Message
     */
    const val MAX_DISPLAYED_OFFSET = 10_000
    const val MAX_DISPLAYED_STRING = "10.000+"
    const val MAX_MESSAGE_IN_BUBBLE = 400

    /*
    * TokoChatPrefManager
     */
    const val TOKOCHAT_CACHE = "tokoChatCache"
}
