package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*

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
    var filter: CalendarFilterDataKeyUiModel = getCalendarFilter(dataKey)
) : BaseWidgetUiModel<CalendarDataUiModel> {

    companion object {
        fun getCalendarFilter(dataKey: String): CalendarFilterDataKeyUiModel {
            val currentDate = Date()
            val firstDayMonthCal = Calendar.getInstance().apply {
                time = currentDate
                set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            }
            val lastDayMonthCal = Calendar.getInstance().apply {
                time = currentDate
                set(Calendar.DAY_OF_MONTH, getActualMinimum(Calendar.DAY_OF_MONTH))
            }
            return CalendarFilterDataKeyUiModel(
                dataKey = dataKey,
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