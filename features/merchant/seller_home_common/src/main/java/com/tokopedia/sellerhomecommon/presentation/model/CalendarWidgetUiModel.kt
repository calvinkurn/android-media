package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class CalendarWidgetUiModel(
    override val id: String,
    override val widgetType: String,
    override val title: String,
    override val subtitle: String,
    override val tooltip: TooltipUiModel?,
    override val tag: String,
    override val appLink: String,
    override val dataKey: String,
    override val ctaText: String,
    override val gridSize: Int,
    override val isShowEmpty: Boolean,
    override var data: CalendarDataUiModel?,
    override var impressHolder: ImpressHolder = ImpressHolder(),
    override var isLoaded: Boolean,
    override var isLoading: Boolean,
    override var isFromCache: Boolean,
    override var isNeedToBeRemoved: Boolean = false,
    override var emptyState: WidgetEmptyStateUiModel,
    override var showLoadingState: Boolean = false,
    var filter: CalendarFilterDataKeyUiModel = getDefaultCalendarFilter(dataKey)
) : BaseWidgetUiModel<CalendarDataUiModel> {

    companion object {
        private fun getDefaultCalendarFilter(dataKey: String): CalendarFilterDataKeyUiModel {
            return CalendarFilterDataKeyUiModel(
                dataKey = dataKey,
                perWeek = getPerWeekDefaultDateRange(),
                perMonth = getPerMonthDefaultDateRange(),
                filterType = DateFilterItem.TYPE_PER_MONTH
            )
        }

        private fun getPerMonthDefaultDateRange(): CalendarFilterDataKeyUiModel.DateRange {
            val currentDate = Date()
            val firstDayMonthCal = Calendar.getInstance().apply {
                time = currentDate
                set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
            }
            val lastDayMonthCal = Calendar.getInstance().apply {
                time = currentDate
                set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            }
            return CalendarFilterDataKeyUiModel.DateRange(
                startDate = DateTimeUtil.format(
                    firstDayMonthCal.timeInMillis,
                    DateTimeUtil.FORMAT_DD_MM_YYYY
                ),
                endDate = DateTimeUtil.format(
                    lastDayMonthCal.timeInMillis,
                    DateTimeUtil.FORMAT_DD_MM_YYYY
                )
            )
        }

        private fun getPerWeekDefaultDateRange(): CalendarFilterDataKeyUiModel.DateRange {
            val sixDaysMillis = TimeUnit.DAYS.toMillis(ShcConst.INT_6.toLong())
            val calendar: Calendar = Calendar.getInstance().apply {
                firstDayOfWeek = Calendar.MONDAY
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
                set(Calendar.MINUTE, ShcConst.INT_0)
                set(Calendar.SECOND, ShcConst.INT_0)
                set(Calendar.MILLISECOND, ShcConst.INT_0)
            }

            val firstDateOfWeek = calendar.time
            val lastDateOfWeek = Date(calendar.timeInMillis.plus(sixDaysMillis))

            return CalendarFilterDataKeyUiModel.DateRange(
                startDate = DateTimeUtil.format(
                    firstDateOfWeek.time,
                    DateTimeUtil.FORMAT_DD_MM_YYYY
                ),
                endDate = DateTimeUtil.format(
                    lastDateOfWeek.time,
                    DateTimeUtil.FORMAT_DD_MM_YYYY
                )
            )
        }
    }

    override fun copyWidget(): BaseWidgetUiModel<CalendarDataUiModel> {
        return copy()
    }

    override fun needToRefreshData(other: BaseWidgetUiModel<CalendarDataUiModel>): Boolean {
        return dataKey != other.dataKey
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}