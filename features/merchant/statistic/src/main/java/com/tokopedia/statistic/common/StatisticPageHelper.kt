package com.tokopedia.statistic.common

import android.content.Context
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.StatisticDateUtil
import com.tokopedia.statistic.view.model.ActionMenuUiModel
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 15/02/21
 */

object StatisticPageHelper {

    fun getShopStatistic(context: Context, userSession: UserSessionInterface): StatisticPageUiModel {
        val title = context.getString(R.string.stc_shop)
        return StatisticPageUiModel(
                pageTitle = title,
                pageSource = Const.PageSource.SHOP_INSIGHT,
                tickerPageName = Const.TickerPageName.SHOP_INSIGHT,
                actionMenu = listOf(
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_give_suggestions),
                                appLink = Const.Url.SHOP_GIVE_SUGGESTIONS,
                                iconUnify = IconUnify.CHAT_REPORT
                        ),
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_learn_more),
                                appLink = Const.Url.SHOP_LEARN_MORE,
                                iconUnify = IconUnify.HELP
                        )
                ),
                dateFilters = getShopDateFilters(context, userSession)
        )
    }

    fun getProductStatistic(context: Context, userSession: UserSessionInterface): StatisticPageUiModel {
        val title = context.getString(R.string.stc_product)
        return StatisticPageUiModel(
                pageTitle = title,
                pageSource = Const.PageSource.PRODUCT_INSIGHT,
                tickerPageName = Const.TickerPageName.PRODUCT_INSIGHT,
                actionMenu = listOf(
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_give_suggestions),
                                appLink = Const.Url.PRODUCT_GIVE_SUGGESTIONS,
                                iconUnify = IconUnify.CHAT_REPORT
                        ),
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_learn_more),
                                appLink = Const.Url.PRODUCT_LEARN_MORE,
                                iconUnify = IconUnify.HELP
                        )
                ),
                dateFilters = getProductDateFilters(context, userSession)
        )
    }

    fun getBuyerStatistic(context: Context, userSession: UserSessionInterface): StatisticPageUiModel {
        val title = context.getString(R.string.stc_buyer)
        return StatisticPageUiModel(
                pageTitle = title,
                pageSource = Const.PageSource.BUYER_INSIGHT,
                tickerPageName = Const.TickerPageName.BUYER_INSIGHT,
                actionMenu = listOf(
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_give_suggestions),
                                appLink = Const.Url.BUYER_GIVE_SUGGESTIONS,
                                iconUnify = IconUnify.CHAT_REPORT
                        ),
                        ActionMenuUiModel(
                                title = context.getString(R.string.stc_learn_more),
                                appLink = Const.Url.BUYER_LEARN_MORE,
                                iconUnify = IconUnify.HELP
                        )
                ),
                dateFilters = getBuyerDateFilters(context, userSession)
        )
    }

    private fun getShopDateFilters(context: Context, userSession: UserSessionInterface): List<DateFilterItem> {
        return if (getRegularMerchantStatus(userSession)) {
            listOf(getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, true))
        } else {
            listOf(
                    getDateRangeItemToday(context, true),
                    getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, false),
                    getDateFilterItemClick(context, Const.DAYS_30, Const.DAYS_30, Const.DAY_1, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
                    DateFilterItem.Divider,
                    getDateFilterPerDay(context),
                    getDateFilterPerWeek(context, false),
                    getFilterPerMonth(context, true),
                    DateFilterItem.ApplyButton
            )
        }
    }

    private fun getProductDateFilters(context: Context, userSession: UserSessionInterface): List<DateFilterItem> {
        return if (getRegularMerchantStatus(userSession)) {
            listOf(getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, true))
        } else {
            listOf(
                    getDateRangeItemToday(context, true),
                    getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, false),
                    getDateFilterItemClick(context, Const.DAYS_30, Const.DAYS_30, Const.DAY_1, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
                    DateFilterItem.Divider,
                    getDateFilterPerDay(context),
                    getDateFilterPerWeek(context, false),
                    getFilterPerMonth(context, true),
                    DateFilterItem.ApplyButton
            )
        }
    }

    private fun getBuyerDateFilters(context: Context, userSession: UserSessionInterface): List<DateFilterItem> {
        return if (getRegularMerchantStatus(userSession)) {
            listOf(getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, true))
        } else {
            listOf(
                    getDateFilterItemClick(context, Const.DAYS_7, Const.DAYS_7, Const.DAY_1, DateFilterItem.TYPE_LAST_7_DAYS, true),
                    getDateFilterItemClick(context, Const.DAYS_30, Const.DAYS_30, Const.DAY_1, DateFilterItem.TYPE_LAST_30_DAYS, showBottomBorder = false),
                    DateFilterItem.Divider,
                    getDateFilterPerWeek(context, true),
                    getFilterPerMonth(context, false),
                    DateFilterItem.ApplyButton
            )
        }
    }

    private fun getDateRangeItemToday(context: Context, isSelected: Boolean): DateFilterItem {
        val label = context.getString(R.string.stc_today_real_time)
        val today = Date()
        return DateFilterItem.Click(label, today, today, isSelected, DateFilterItem.TYPE_TODAY)
    }

    private fun getDateFilterItemClick(context: Context, lastNDaysLabel: Int, startPastDays: Int, endPastDays: Int, type: Int, isSelected: Boolean = false, showBottomBorder: Boolean = true): DateFilterItem.Click {
        val label: String = context.getString(R.string.stc_last_n_days, lastNDaysLabel)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(startPastDays.toLong()))
        val endDate = Date(DateTimeUtil.getNPastDaysTimestamp(endPastDays.toLong()))
        return DateFilterItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateFilterPerDay(context: Context): DateFilterItem.Pick {
        val label = context.getString(R.string.stc_per_day)
        val today = Date()
        return DateFilterItem.Pick(label, today, today, type = DateFilterItem.TYPE_PER_DAY, calendarPickerMaxDate = today)
    }

    private fun getDateFilterPerWeek(context: Context, isOnlyCompletedWeek: Boolean): DateFilterItem.Pick {
        val calendar: Calendar = Calendar.getInstance()
        with(calendar) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var firstDateOfWeek = calendar.time
        var lastDateOfWeek = Date()

        if (isOnlyCompletedWeek) {
            val sevenDaysMillis = TimeUnit.DAYS.toMillis(7)
            val sixDaysMillis = TimeUnit.DAYS.toMillis(6)
            val firstDateOfWeekTimeStamp = calendar.time.time.minus(sevenDaysMillis)
            firstDateOfWeek = Date(firstDateOfWeekTimeStamp)
            lastDateOfWeek = Date(firstDateOfWeekTimeStamp.plus(sixDaysMillis))
        }

        val label = context.getString(R.string.stc_per_week)
        return DateFilterItem.Pick(label, firstDateOfWeek, lastDateOfWeek, type = DateFilterItem.TYPE_PER_WEEK, calendarPickerMaxDate = lastDateOfWeek)
    }

    private fun getFilterPerMonth(context: Context, canSelectOnGoingMonth: Boolean): DateFilterItem.MonthPickerItem {
        val perMonthLabel = context.getString(R.string.stc_per_month)
        val defaultDate: Date = if (canSelectOnGoingMonth) {
            Date()
        } else {
            Date().apply {
                val millisOf31Days = TimeUnit.DAYS.toMillis(31)
                time = time.minus(millisOf31Days)
            }
        }
        val (startDate, endDate) = StatisticDateUtil.getStartAndEndDateInAMonth(defaultDate)
        return DateFilterItem.MonthPickerItem(perMonthLabel, startDate = startDate, endDate = endDate, monthPickerMaxDate = defaultDate)
    }

    fun getRegularMerchantStatus(userSession: UserSessionInterface): Boolean {
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        val isOfficialStore = userSession.isShopOfficialStore
        return !isPowerMerchant && !isOfficialStore
    }
}