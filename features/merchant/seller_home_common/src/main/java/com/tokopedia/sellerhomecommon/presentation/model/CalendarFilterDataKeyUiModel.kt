package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class CalendarFilterDataKeyUiModel(
    val dataKey: String = "",
    val perWeek: DateRange = DateRange(),
    val perMonth: DateRange = DateRange(),
    val filterType: Int = DateFilterItem.TYPE_PER_MONTH
) {
    data class DateRange(
        val startDate: String = "",
        val endDate: String = ""
    )

    fun getDateRange(): DateRange {
        return if (filterType == DateFilterItem.TYPE_PER_MONTH) {
            perMonth
        } else {
            perWeek
        }
    }
}