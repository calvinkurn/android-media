package com.tokopedia.abstraction.common.utils.time

import com.tokopedia.abstraction.common.utils.time.RfcDateTimeParser
import com.tokopedia.abstraction.common.utils.time.RfcDateTimeParser.RFC_3339

object DateHelper {

    @JvmStatic
    fun timeSince(startTime: String, endTime: String): Long {
        val start = RfcDateTimeParser.parseDateString(startTime, RFC_3339)?.time ?: 0
        val end = RfcDateTimeParser.parseDateString(endTime, RFC_3339)?.time ?: 0

        val diff = end - start
        return if (diff > 0) {
            diff
        } else {
            0
        }
    }
}