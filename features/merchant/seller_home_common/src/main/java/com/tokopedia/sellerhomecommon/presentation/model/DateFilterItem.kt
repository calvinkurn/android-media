package com.tokopedia.sellerhomecommon.presentation.model

import android.content.Context
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.DateFilterAdapterFactory
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

sealed class DateFilterItem(
    open val label: String = "",
    open val startDate: Date? = null,
    open val endDate: Date? = null,
    open var isSelected: Boolean = false,
    open val type: Int
) : Visitable<DateFilterAdapterFactory>, Parcelable {

    companion object {
        const val TYPE_TODAY = 0
        const val TYPE_LAST_7_DAYS = 1
        const val TYPE_LAST_30_DAYS = 2
        const val TYPE_PER_DAY = 3
        const val TYPE_PER_WEEK = 4
        const val TYPE_PER_MONTH = 5
        const val TYPE_BUTTON = 6
        const val TYPE_DIVIDER = 7
        const val TYPE_CUSTOM = 8
        const val TYPE_CUSTOM_SAME_MONTH = 9
    }

    fun getHeaderSubTitle(context: Context): String {
        when (type) {
            TYPE_TODAY -> {
                val startDateMillis = startDate?.time ?: return ""
                val dateStr = DateTimeUtil.format(startDateMillis, DateTimeUtil.FORMAT_DD_MMM)
                val hourStr = DateTimeUtil.format(
                    System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(ShcConst.INT_1.toLong())),
                    DateTimeUtil.FORMAT_HOUR_24
                )
                return context.getString(R.string.shc_today_fmt, dateStr, hourStr)
            }
            TYPE_LAST_7_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.shc_last_7_days_fmt, dateRangeStr)
            }
            TYPE_LAST_30_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.shc_last_30_days_fmt, dateRangeStr)
            }
            TYPE_PER_DAY -> {
                val startDateMillis = startDate?.time ?: return ""
                val perDayFmt = DateTimeUtil.format(
                    startDateMillis, DateTimeUtil.FORMAT_DD_MMM_YYYY
                )
                return context.getString(R.string.shc_per_day_fmt, perDayFmt)
            }
            TYPE_PER_WEEK -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.shc_per_week_fmt, dateRangeStr)
            }
            TYPE_PER_MONTH -> {
                startDate?.let {
                    val monthFmt = DateTimeUtil.format(it.time, DateTimeUtil.FORMAT_MMMM_YYYY)
                    return context.getString(R.string.shc_per_month_fmt, monthFmt)
                }
                return context.getString(R.string.shc_per_month)
            }
            TYPE_CUSTOM -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.shc_custom, dateRangeStr)
            }
        }
        return ""
    }

    fun getDateFilterType(): String {
        return when (type) {
            TYPE_TODAY -> DateFilterType.DATE_TYPE_TODAY
            TYPE_PER_WEEK -> DateFilterType.DATE_TYPE_WEEK
            TYPE_PER_MONTH -> DateFilterType.DATE_TYPE_MONTH
            else -> DateFilterType.DATE_TYPE_DAY
        }
    }

    @Parcelize
    data class Click(
        override val label: String,
        override val startDate: Date,
        override val endDate: Date,
        override var isSelected: Boolean = false,
        override val type: Int,
        val showBottomBorder: Boolean = true
    ) : DateFilterItem(label, startDate, endDate, isSelected, type) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    @Parcelize
    data class Pick(
        override val label: String,
        override var startDate: Date? = null,
        override var endDate: Date? = null,
        override var isSelected: Boolean = false,
        override val type: Int,
        val calendarPickerMinDate: Date? = null,
        val calendarPickerMaxDate: Date? = null
    ) : DateFilterItem(label, startDate, endDate, isSelected, type) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    @Parcelize
    object ApplyButton : DateFilterItem(type = TYPE_BUTTON) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    @Parcelize
    object Divider : DateFilterItem(type = TYPE_DIVIDER) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    @Parcelize
    data class MonthPickerItem(
        override val label: String,
        override var startDate: Date? = null,
        override var endDate: Date? = null,
        override var isSelected: Boolean = false,
        val monthPickerMinDate: Date? = null,
        val monthPickerMaxDate: Date? = null
    ) : DateFilterItem(type = TYPE_PER_MONTH) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}