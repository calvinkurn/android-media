package com.tokopedia.statistic.common.utils

import java.util.*

object StatisticDateUtil {

    /**
     * Get first and last date in the selected month. For example: February 2020 -> return 01 February 2020 and 29 February 2020
     *
     * @param   selectedMonth   date of the selected month
     * @return  pair of first and last date of a month
     */
    internal fun getStartAndEndDateInAMonth(selectedMonth: Date): Pair<Date?, Date?> {
        val startCalendar = getNewCalendarInstance(selectedMonth)
        val endCalendar = getNewCalendarInstance(selectedMonth)
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH))
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return startCalendar.time to endCalendar.time
    }

    private fun getNewCalendarInstance(selectedMonth: Date): Calendar =
            Calendar.getInstance().apply {
                time = selectedMonth
            }

}