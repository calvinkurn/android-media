package com.tokopedia.statistic.utils

import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*

/**
 * Created By @ilhamsuaib on 23/06/20
 */

object DateRangeFormatUtil {

    private const val PATTERN_DAY = "dd"
    private const val PATTERN_MONTH_MM = "MM"
    private const val PATTERN_MONTH_MMM = "MMM"
    private const val PATTERN_MONTH_MMMM = "MMMM"
    private const val PATTERN_YEAR = "yyyy"

    fun getDateRangeStr(startDate: Date, endDate: Date): String {
        val startMonth = DateTimeUtil.format(startDate.time, PATTERN_MONTH_MM)
        val endMonth = DateTimeUtil.format(endDate.time, PATTERN_MONTH_MM)
        val startYear = DateTimeUtil.format(startDate.time, PATTERN_YEAR)
        val endYear = DateTimeUtil.format(endDate.time, PATTERN_YEAR)

        if (areStartAndEndDateSame(startDate, endDate)) {
            val hourStr = DateTimeUtil.format(System.currentTimeMillis(), "hh:mm")
            val singleDayPattern = "$PATTERN_DAY $PATTERN_MONTH_MMMM (00:00 - $hourStr)"
            return DateTimeUtil.format(startDate.time, pattern = singleDayPattern)
        }

        val startDatePattern: String
        val endDatePattern: String
        when {
            startMonth == endMonth && startYear == endYear -> {
                //ex : 12 - 15 Apr 2020
                startDatePattern = PATTERN_DAY
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
            startMonth != endMonth && startYear == endYear -> {
                //ex : 12 Jan - 15 Apr 2020
                startDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM"
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
            else -> {
                //ex : 12 Jan 2020 - 15 Apr 2020
                startDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
                endDatePattern = "$PATTERN_DAY $PATTERN_MONTH_MMM $PATTERN_YEAR"
            }
        }

        val startDateStr = DateTimeUtil.format(startDate.time, pattern = startDatePattern)
        val endDateStr = DateTimeUtil.format(endDate.time, pattern = endDatePattern)

        return "$startDateStr - $endDateStr"
    }

    private fun areStartAndEndDateSame(startDate: Date, endDate: Date): Boolean {
        val pattern = "$PATTERN_DAY $PATTERN_MONTH_MM $PATTERN_YEAR"
        val startDateStr = DateTimeUtil.format(startDate.time, pattern = pattern)
        val endDateStr = DateTimeUtil.format(endDate.time, pattern = pattern)
        return startDateStr == endDateStr
    }
}