package com.tokopedia.statistic.common

import android.content.Context
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.StatisticDateUtil
import com.tokopedia.statistic.common.utils.StatisticRemoteConfig
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

    fun getShopStatistic(
        context: Context,
        remoteConfig: StatisticRemoteConfig
    ): StatisticPageUiModel {
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
            dateFilters = getShopDateFilters(context, remoteConfig),
            exclusiveIdentifierDateFilterDesc = context.getString(R.string.stc_shop_exclusive_identifier_desc)
        )
    }

    fun getProductStatistic(
        context: Context,
        remoteConfig: StatisticRemoteConfig
    ): StatisticPageUiModel {
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
            dateFilters = getProductDateFilters(context, remoteConfig),
            exclusiveIdentifierDateFilterDesc = context.getString(R.string.stc_product_exclusive_identifier_desc)
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
                    appLink = Const.Url.BUYER_GIVE_SUGGESTIONS,
                    iconUnify = IconUnify.CHAT_REPORT
                ),
                ActionMenuUiModel(
                    title = context.getString(R.string.stc_learn_more),
                    appLink = Const.Url.BUYER_LEARN_MORE,
                    iconUnify = IconUnify.HELP
                )
            ),
            dateFilters = getBuyerDateFilters(context),
            exclusiveIdentifierDateFilterDesc = context.getString(R.string.stc_buyer_and_operational_exclusive_identifier_desc)
        )
    }

    fun getOperationalStatistic(context: Context): StatisticPageUiModel {
        val title = context.getString(R.string.stc_operational)
        return StatisticPageUiModel(
            pageTitle = title,
            pageSource = Const.PageSource.OPERATIONAL_INSIGHT,
            tickerPageName = Const.TickerPageName.OPERATIONAL_INSIGHT,
            actionMenu = listOf(
                ActionMenuUiModel(
                    title = context.getString(R.string.stc_give_suggestions),
                    appLink = Const.Url.OPERATIONAL_GIVE_SUGGESTIONS,
                    iconUnify = IconUnify.CHAT_REPORT
                ),
                ActionMenuUiModel(
                    title = context.getString(R.string.stc_learn_more),
                    appLink = Const.Url.OPERATIONAL_LEARN_MORE,
                    iconUnify = IconUnify.HELP
                )
            ),
            dateFilters = getOperationalDateFilters(context),
            exclusiveIdentifierDateFilterDesc = context.getString(R.string.stc_buyer_and_operational_exclusive_identifier_desc)
        )
    }

    fun getTrafficStatistic(context: Context): StatisticPageUiModel {
        val title = context.getString(R.string.stc_traffic)
        return StatisticPageUiModel(
            pageTitle = title,
            pageSource = Const.PageSource.TRAFFIC_INSIGHT,
            tickerPageName = Const.TickerPageName.TRAFFIC_INSIGHT,
            shouldShowTag = getTrafficShowTagStatus(),
            actionMenu = listOf(
                ActionMenuUiModel(
                    title = context.getString(R.string.stc_give_suggestions),
                    appLink = Const.Url.TRAFFIC_GIVE_SUGGESTIONS,
                    iconUnify = IconUnify.CHAT_REPORT
                ),
                ActionMenuUiModel(
                    title = context.getString(R.string.stc_learn_more),
                    appLink = Const.Url.TRAFFIC_LEARN_MORE,
                    iconUnify = IconUnify.HELP
                )
            ),
            dateFilters = getOperationalDateFilters(context),
            exclusiveIdentifierDateFilterDesc = context.getString(R.string.stc_buyer_and_operational_exclusive_identifier_desc)
        )
    }

    fun getRegularMerchantStatus(userSession: UserSessionInterface): Boolean {
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        val isOfficialStore = userSession.isShopOfficialStore
        return !isPowerMerchant && !isOfficialStore
    }

    private fun getShopDateFilters(
        context: Context,
        remoteConfig: StatisticRemoteConfig
    ): List<DateFilterItem> {
        val filters = mutableListOf(
            getDateRangeItemToday(context, true),
            getDateFilterItemClick(
                context,
                Const.DAYS_7,
                Const.DAYS_7,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_7_DAYS,
                false
            ),
            getDateFilterItemClick(
                context,
                Const.DAYS_30,
                Const.DAYS_30,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_30_DAYS,
                showBottomBorder = false
            ),
            DateFilterItem.Divider,
            getDateFilterPerDay(context, Const.DAYS_365),
            getDateFilterPerWeek(context, false, Const.DAYS_365),
            getFilterPerMonth(context, true, Const.DAYS_365),
        )

        if (remoteConfig.isCustomDateFilterEnabled()) {
            filters.add(
                getDateFilterCustom(
                    context,
                    Const.DAYS_365,
                    DateFilterItem.TYPE_CUSTOM
                )
            )
        }

        filters.add(DateFilterItem.ApplyButton)
        return filters
    }

    private fun getProductDateFilters(
        context: Context,
        remoteConfig: StatisticRemoteConfig
    ): List<DateFilterItem> {
        val filters = mutableListOf(
            getDateRangeItemToday(context, false),
            getDateFilterItemClick(
                context,
                Const.DAYS_7,
                Const.DAYS_7,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_7_DAYS,
                true
            ),
            getDateFilterItemClick(
                context,
                Const.DAYS_30,
                Const.DAYS_30,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_30_DAYS,
                showBottomBorder = false
            ),
            DateFilterItem.Divider,
            getDateFilterPerDay(context, Const.DAYS_365),
            getDateFilterPerWeek(context, false, Const.DAYS_365),
            getFilterPerMonth(context, true, Const.DAYS_365)
        )

        if (remoteConfig.isCustomDateFilterEnabled()) {
            filters.add(
                getDateFilterCustom(
                    context,
                    Const.DAYS_365,
                    DateFilterItem.TYPE_CUSTOM_SAME_MONTH
                )
            )
        }

        filters.add(DateFilterItem.ApplyButton)
        return filters
    }

    private fun getBuyerDateFilters(context: Context): List<DateFilterItem> {
        return listOf(
            getDateFilterItemClick(
                context,
                Const.DAYS_7,
                Const.DAYS_7,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_7_DAYS,
                true
            ),
            getDateFilterItemClick(
                context,
                Const.DAYS_30,
                Const.DAYS_30,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_30_DAYS,
                showBottomBorder = false
            ),
            DateFilterItem.Divider,
            getDateFilterPerWeek(context, true, Const.DAYS_91),
            getFilterPerMonth(context, false, Const.DAYS_91),
            DateFilterItem.ApplyButton
        )
    }

    private fun getOperationalDateFilters(context: Context): List<DateFilterItem> {
        return listOf(
            getDateFilterItemClick(
                context,
                Const.DAYS_7,
                Const.DAYS_7,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_7_DAYS,
                true
            ),
            getDateFilterItemClick(
                context,
                Const.DAYS_30,
                Const.DAYS_30,
                Const.DAY_1,
                DateFilterItem.TYPE_LAST_30_DAYS,
                showBottomBorder = false
            ),
            DateFilterItem.Divider,
            getDateFilterPerWeek(context, true, Const.DAYS_91),
            getFilterPerMonth(context, false, Const.DAYS_91),
            DateFilterItem.ApplyButton
        )
    }

    private fun getTrafficDateFilters(context: Context): List<DateFilterItem> {
        return listOf(
            getDateFilterItemClick(
                context,
                Const.DAYS_7,
                Const.DAYS_7,
                Const.DAY_1,
                type = DateFilterItem.TYPE_LAST_7_DAYS,
                true
            ),
            getDateFilterItemClick(
                context,
                Const.DAYS_30,
                Const.DAYS_30,
                Const.DAY_1,
                type = DateFilterItem.TYPE_LAST_30_DAYS,
                showBottomBorder = false
            ),
            DateFilterItem.Divider,
            getDateFilterPerDay(context, Const.DAYS_365),
            getDateFilterPerWeek(context, true, Const.DAYS_91),
            getFilterPerMonth(context, false, Const.DAYS_91),
            DateFilterItem.ApplyButton
        )
    }

    private fun getDateRangeItemToday(context: Context, isSelected: Boolean): DateFilterItem {
        val label = context.getString(R.string.stc_today_real_time)
        val today = Date()
        return DateFilterItem.Click(label, today, today, isSelected, DateFilterItem.TYPE_TODAY)
    }

    private fun getDateFilterItemClick(
        context: Context,
        lastNDaysLabel: Int,
        startPastDays: Int,
        endPastDays: Int,
        type: Int,
        isSelected: Boolean = false,
        showBottomBorder: Boolean = true
    ): DateFilterItem.Click {
        val label: String = context.getString(R.string.stc_last_n_days, lastNDaysLabel)
        val startDate = Date(DateTimeUtil.getNPastDaysTimestamp(startPastDays.toLong()))
        val endDate = Date(DateTimeUtil.getNPastDaysTimestamp(endPastDays.toLong()))
        return DateFilterItem.Click(label, startDate, endDate, isSelected, type, showBottomBorder)
    }

    private fun getDateFilterPerDay(context: Context, minDaysCount: Int): DateFilterItem.Pick {
        val label = context.getString(R.string.stc_per_day)
        val today = Date()
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(minDaysCount.toLong()))
        return DateFilterItem.Pick(
            label,
            today,
            today,
            type = DateFilterItem.TYPE_PER_DAY,
            calendarPickerMinDate = minDate,
            calendarPickerMaxDate = today
        )
    }

    private fun getDateFilterCustom(
        context: Context,
        minDaysCount: Int,
        type: Int
    ): DateFilterItem.Pick {
        val label = context.getString(R.string.stc_custom_lbl)
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(minDaysCount.toLong()))
        val maxDate = Date(DateTimeUtil.getNPastDaysTimestamp(Const.DAY_1.toLong()))
        return DateFilterItem.Pick(
            label,
            null,
            null,
            type = type,
            calendarPickerMinDate = minDate,
            calendarPickerMaxDate = maxDate
        )
    }

    private fun getDateFilterPerWeek(
        context: Context,
        isOnlyCompletedWeek: Boolean,
        minDaysCount: Int
    ): DateFilterItem.Pick {
        val calendar: Calendar = Calendar.getInstance()
        with(calendar) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, Const.EMPTY)
            set(Calendar.MINUTE, Const.EMPTY)
            set(Calendar.SECOND, Const.EMPTY)
            set(Calendar.MILLISECOND, Const.EMPTY)
        }
        var firstDateOfWeek = calendar.time
        var lastDateOfWeek = Date()

        if (isOnlyCompletedWeek) {
            val sevenDaysMillis = TimeUnit.DAYS.toMillis(Const.DAYS_7.toLong())
            val sixDaysMillis = TimeUnit.DAYS.toMillis(Const.DAYS_6.toLong())
            val firstDateOfWeekTimeStamp = calendar.time.time.minus(sevenDaysMillis)
            firstDateOfWeek = Date(firstDateOfWeekTimeStamp)
            lastDateOfWeek = Date(firstDateOfWeekTimeStamp.plus(sixDaysMillis))
        }

        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(minDaysCount.toLong()))

        val label = context.getString(R.string.stc_per_week)
        return DateFilterItem.Pick(
            label, firstDateOfWeek, lastDateOfWeek, type = DateFilterItem.TYPE_PER_WEEK,
            calendarPickerMinDate = minDate, calendarPickerMaxDate = lastDateOfWeek
        )
    }

    private fun getFilterPerMonth(
        context: Context,
        canSelectOnGoingMonth: Boolean,
        minDaysCount: Int
    ): DateFilterItem.MonthPickerItem {
        val perMonthLabel = context.getString(R.string.stc_per_month)
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(minDaysCount.toLong()))
        val defaultDate: Date = if (canSelectOnGoingMonth) {
            Date()
        } else {
            Date().apply {
                val millisOf31Days = TimeUnit.DAYS.toMillis(Const.DAYS_31.toLong())
                time = time.minus(millisOf31Days)
            }
        }
        val (startDate, endDate) = StatisticDateUtil.getStartAndEndDateInAMonth(defaultDate)
        return DateFilterItem.MonthPickerItem(
            perMonthLabel,
            startDate = startDate,
            endDate = endDate,
            monthPickerMinDate = minDate,
            monthPickerMaxDate = defaultDate
        )
    }

    private fun getTrafficShowTagStatus(): Boolean {
        val removeTagAfter: Long = 1649606400000
        val now = Date().time
        return now <= removeTagAfter
    }
}