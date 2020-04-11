package com.tokopedia.utils.time

import com.tokopedia.utils.time.RfcDateTimeParser.RFC_3339
import java.util.*

object TimeHelper {

    @JvmStatic
    fun timeBetweenRFC3339(startTime: String, endTime: String): Long {
        val start = RfcDateTimeParser.parseDateString(startTime, RFC_3339)?.time ?: 0
        val end = RfcDateTimeParser.parseDateString(endTime, RFC_3339)?.time ?: 0

        val diff = end - start
        return if (diff > 0) {
            diff
        } else {
            0
        }
    }

    @JvmStatic
    fun timeSinceNow(endTime: String): Long {
        val start = Calendar.getInstance().time.time
        val end = RfcDateTimeParser.parseDateString(endTime, RFC_3339)?.time ?: 0

        return end - start
    }
}