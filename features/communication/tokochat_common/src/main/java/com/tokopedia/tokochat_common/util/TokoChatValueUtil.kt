package com.tokopedia.tokochat_common.util

object TokoChatValueUtil {
    /**
     * Role Value
     */
    const val DRIVER = "driver"

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
     * Compose Message
     */
    const val MAX_DISPLAYED_OFFSET = 1_000
    const val MAX_DISPLAYED_STRING = "1000+"
}
