package com.tokopedia.review.feature.reputationhistory.util

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*

object ReputationPenaltyDateUtils {
    fun format(timeMillis: Long, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, DateFormatUtils.DEFAULT_LOCALE)
        return sdf.format(timeMillis)
    }

    fun getPastDaysReputationPenaltyDate(totalDays: Int): Date {
        val date = Calendar.getInstance(DateFormatUtils.DEFAULT_LOCALE)
        date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - totalDays)
        return date.time
    }
}