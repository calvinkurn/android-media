package com.tokopedia.minicart.bmgm.common.utils

import com.tokopedia.utils.time.DateFormatUtils
import java.util.Date

/**
 * Created by @ilhamsuaib on 12/09/23.
 */

object MiniCartUtils {

    fun checkIsOfferEnded(offerEndDate: String): Boolean {
        if (offerEndDate.isBlank()) {
            return false
        }
        val offerEndInMilliseconds = DateFormatUtils.getTimeInMilliseconds(
            format = DateFormatUtils.FORMAT_YYYY_MM_DD_HH_mm_ss,
            dateString = offerEndDate
        )

        if (offerEndInMilliseconds == DateFormatUtils.INVALID_TIME_IN_MILLIS) {
            return false
        }

        val nowMillis = Date().time
        return offerEndInMilliseconds <= nowMillis
    }
}