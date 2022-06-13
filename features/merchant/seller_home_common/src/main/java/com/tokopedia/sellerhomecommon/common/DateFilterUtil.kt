package com.tokopedia.sellerhomecommon.common

import android.content.Context
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

object DateFilterUtil {

    private const val PATTERN_DAY = "dd"
    private const val PATTERN_MONTH_MM = "MM"
    private const val PATTERN_MONTH_MMM = "MMM"
    private const val PATTERN_MONTH_MMMM = "MMMM"
    private const val PATTERN_YEAR = "yyyy"
    private const val PATTERN_HOURS_24 = "HH:00"

    fun getDateRangeStr(startDate: Date, endDate: Date): String {
        val startMonth = DateTimeUtil.format(startDate.time, PATTERN_MONTH_MM)
        val endMonth = DateTimeUtil.format(endDate.time, PATTERN_MONTH_MM)
        val startYear = DateTimeUtil.format(startDate.time, PATTERN_YEAR)
        val endYear = DateTimeUtil.format(endDate.time, PATTERN_YEAR)

        if (areStartAndEndDateSame(startDate, endDate)) {
            val hourStr = DateTimeUtil.format(
                System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)),
                PATTERN_HOURS_24
            )
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

    /**
     * Get first and last date in the selected month. For example: February 2020 -> return 01 February 2020 and 29 February 2020
     *
     * @param   selectedMonth   date of the selected month
     * @return  pair of first and last date of a month
     */
    fun getStartAndEndDateInAMonth(selectedMonth: Date): Pair<Date?, Date?> {
        val startCalendar = getNewCalendarInstance(selectedMonth)
        val endCalendar = getNewCalendarInstance(selectedMonth)
        startCalendar.set(
            Calendar.DAY_OF_MONTH,
            startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        )
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return startCalendar.time to endCalendar.time
    }

    private fun getNewCalendarInstance(selectedMonth: Date): Calendar {
        return Calendar.getInstance().apply {
            time = selectedMonth
        }
    }

    private fun areStartAndEndDateSame(startDate: Date, endDate: Date): Boolean {
        val pattern = "$PATTERN_DAY $PATTERN_MONTH_MM $PATTERN_YEAR"
        val startDateStr = DateTimeUtil.format(startDate.time, pattern = pattern)
        val endDateStr = DateTimeUtil.format(endDate.time, pattern = pattern)
        return startDateStr == endDateStr
    }

    object FilterList {

        fun getCalendarPickerFilterList(
            context: Context,
            perWeek: Date,
            perMonth: Date,
            filterType: Int
        ): List<DateFilterItem> {
            val filters = listOf(
                getDateFilterPerWeek(context, perWeek),
                getFilterPerMonth(context, perMonth),
                DateFilterItem.ApplyButton
            )
            filters.forEach {
                it.isSelected = it.type == filterType
            }
            return filters
        }

        private fun getDateFilterPerWeek(
            context: Context,
            selectedDate: Date
        ): DateFilterItem.Pick {
            val sixDaysMillis = TimeUnit.DAYS.toMillis(ShcConst.INT_6.toLong())

            val maxDateCal = Calendar.getInstance().apply {
                time = Date(DateTimeUtil.getNNextDaysTimestamp(ShcConst.INT_120.toLong()))
                firstDayOfWeek = Calendar.MONDAY
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
                set(Calendar.MINUTE, ShcConst.INT_0)
                set(Calendar.SECOND, ShcConst.INT_0)
                set(Calendar.MILLISECOND, ShcConst.INT_0)
            }

            val minDateCal = Calendar.getInstance().apply {
                time = Date(DateTimeUtil.getNPastDaysTimestamp(ShcConst.INT_30.toLong()))
                firstDayOfWeek = Calendar.MONDAY
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
                set(Calendar.MINUTE, ShcConst.INT_0)
                set(Calendar.SECOND, ShcConst.INT_0)
                set(Calendar.MILLISECOND, ShcConst.INT_0)
            }

            val calendar: Calendar = Calendar.getInstance().apply {
                time = selectedDate
                firstDayOfWeek = Calendar.MONDAY
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
                set(Calendar.MINUTE, ShcConst.INT_0)
                set(Calendar.SECOND, ShcConst.INT_0)
                set(Calendar.MILLISECOND, ShcConst.INT_0)
            }

            val firstDateOfWeek = calendar.time
            val lastDateOfWeek = Date(calendar.timeInMillis.plus(sixDaysMillis))
            val minDate = minDateCal.time
            val maxDate = Date(maxDateCal.timeInMillis.plus(sixDaysMillis))

            return DateFilterItem.Pick(
                label = context.getString(R.string.shc_per_week),
                startDate = firstDateOfWeek,
                endDate = lastDateOfWeek,
                type = DateFilterItem.TYPE_PER_WEEK,
                calendarPickerMinDate = minDate,
                calendarPickerMaxDate = maxDate
            )
        }

        private fun getFilterPerMonth(
            context: Context,
            defaultDate: Date
        ): DateFilterItem.MonthPickerItem {
            val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(ShcConst.INT_30.toLong()))
            val maxDate = Date(DateTimeUtil.getNNextDaysTimestamp(ShcConst.INT_120.toLong()))
            val (startDate, endDate) = getStartAndEndDateInAMonth(defaultDate)
            return DateFilterItem.MonthPickerItem(
                label = context.getString(R.string.shc_per_month),
                startDate = startDate,
                endDate = endDate,
                monthPickerMinDate = minDate,
                monthPickerMaxDate = maxDate
            )
        }
    }
}