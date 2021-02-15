package com.tokopedia.statistic.common

import android.content.Context
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.ActionMenuUiModel
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import java.util.*

/**
 * Created By @ilhamsuaib on 15/02/21
 */

object StatisticPageHelper {

    fun getShopStatistic(context: Context): StatisticPageUiModel {
        val title = context.getString(R.string.stc_shop)
        return StatisticPageUiModel(
                pageTitle = title,
                pageSource = Const.PageSource.SHOP_INSIGHT,
                tickerPageName = Const.TickerPageName.SHOP_INSIGHT,
                actionMenu = listOf(
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_give_suggestions),
                                appLink = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU",
                                iconUnify = IconUnify.INFORMATION
                        ),
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_learn_more),
                                appLink = "https://www.tokopedia.com/help/article/apa-itu-statistik-toko?source=sapp-wawasan-toko",
                                iconUnify = IconUnify.INFORMATION
                        )
                ),
                dateFilters = getShopDateFilters(context)
        )
    }

    fun getBuyerStatistic(context: Context): StatisticPageUiModel {
        val title = context.getString(R.string.stc_buyer)
        return StatisticPageUiModel(
                pageTitle = title,
                pageSource = Const.PageSource.BUYER_INSIGHT,
                tickerPageName = Const.TickerPageName.BUYER_INSIGHT,
                actionMenu = listOf(
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_give_suggestions),
                                appLink = "https://docs.google.com/forms/d/1g16aH6t8n6k-jMqOZpDK4QVgaxIXNodclNpwhS9KdkU/edit",
                                iconUnify = IconUnify.INFORMATION
                        ),
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_learn_more),
                                appLink = "https://www.tokopedia.com/help/article/apa-itu-wawasan-pembeli?source=sapp-wawasan-pembeli",
                                iconUnify = IconUnify.INFORMATION
                        )
                ),
                dateFilters = getBuyerDateFilters(context)
        )
    }

    private fun getShopDateFilters(context: Context): List<DateFilterItem> = listOf(
            getDateRangeItemToday(context),
            getDateFilterItemClick(context, Const.DAYS_6, Const.DAY_0, DateFilterItem.TYPE_LAST_7_DAYS, true),
            getDateFilterItemClick(context, Const.DAYS_29, Const.DAY_0, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
            DateFilterItem.Divider,
            getDateFilterPerDay(context),
            getDateFilterPerWeek(context),
            getFilterPerMonth(context),
            DateFilterItem.ApplyButton
    )

    private fun getBuyerDateFilters(context: Context): List<DateFilterItem> = listOf(
            getDateFilterItemClick(context, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, true),
            getDateFilterItemClick(context, Const.DAYS_30, Const.DAY_1, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
            DateFilterItem.Divider,
            getDateFilterPerWeek(context),
            getFilterPerMonth(context),
            DateFilterItem.ApplyButton
    )

    private fun getDateRangeItemToday(context: Context): DateFilterItem {
        val label = context.getString(R.string.stc_today_real_time)
        val today = Date()
        return DateFilterItem.Click(label, today, today, false, DateFilterItem.TYPE_TODAY)
    }

    private fun getDateFilterItemClick(context: Context, startPastDays: Int, endPastDays: Int, type: Int, isSelected: Boolean = false, showBottomBorder: Boolean = true): DateFilterItem.Click {
        val label: String = context.getString(R.string.stc_last_n_days, startPastDays)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(startPastDays.toLong()))
        val endDate = Date(DateTimeUtil.getNPastDaysTimestamp(endPastDays.toLong()))
        return DateFilterItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateFilterPerDay(context: Context): DateFilterItem.Pick {
        val label = context.getString(R.string.stc_per_day)
        val today = Date()
        return DateFilterItem.Pick(label, today, today, type = DateFilterItem.TYPE_PER_DAY)
    }

    private fun getDateFilterPerWeek(context: Context): DateFilterItem.Pick {
        val calendar: Calendar = Calendar.getInstance()
        with(calendar) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val firstDateOfWeek = calendar.time
        val lastDateOfWeek = Date()
        val label = context.getString(R.string.stc_per_week)
        return DateFilterItem.Pick(label, firstDateOfWeek, lastDateOfWeek, type = DateFilterItem.TYPE_PER_WEEK)
    }

    private fun getFilterPerMonth(context: Context): DateFilterItem.MonthPickerItem {
        val perMonthLabel = context.getString(R.string.stc_per_month)
        return DateFilterItem.MonthPickerItem(perMonthLabel, startDate = Date(), endDate = Date())
    }
}