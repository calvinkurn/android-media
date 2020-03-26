package com.tokopedia.purchase_platform.features.checkout.view.helper

object DateHelper {

    @JvmStatic
    fun timeSince(startTime: String, endTime: String): Long {
        val start = RfcDateTimeParser.parseDateString(startTime, RfcDateTimeParser.RFC_3339)?.time ?: 0
        val end = RfcDateTimeParser.parseDateString(endTime, RfcDateTimeParser.RFC_3339)?.time ?: 0

        val diff = end - start
        if (diff > 0) {
            return diff
        } else {
            return 0
        }
    }
}