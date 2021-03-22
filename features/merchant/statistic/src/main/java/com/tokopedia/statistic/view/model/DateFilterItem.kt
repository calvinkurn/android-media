package com.tokopedia.statistic.view.model

import android.content.Context
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.statistic.view.adapter.factory.DateFilterAdapterFactory
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 15/06/20
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
    }

    fun getHeaderSubTitle(context: Context): String {
        when (type) {
            TYPE_TODAY -> {
                val startDateMillis = startDate?.time ?: return ""
                val dateStr = DateTimeUtil.format(startDateMillis, "dd MMMM")
                val hourStr = DateTimeUtil.format(System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)), "HH:00")
                return context.getString(R.string.stc_today_fmt, dateStr, hourStr)
            }
            TYPE_LAST_7_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.stc_last_7_days_fmt, dateRangeStr)
            }
            TYPE_LAST_30_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.stc_last_30_days_fmt, dateRangeStr)
            }
            TYPE_PER_DAY -> {
                val startDateMillis = startDate?.time ?: return ""
                val perDayFmt = DateTimeUtil.format(startDateMillis, "dd MMM yyyy")
                return context.getString(R.string.stc_per_day_fmt, perDayFmt)
            }
            TYPE_PER_WEEK -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return context.getString(R.string.stc_per_week_fmt, dateRangeStr)
            }
            TYPE_PER_MONTH -> {
                startDate?.let {
                    val monthFmt = DateTimeUtil.format(it.time, "MMMM yyyy")
                    return context.getString(R.string.stc_per_month_fmt, monthFmt)
                }
                return context.getString(R.string.stc_per_month)
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
            val monthPickerMaxDate: Date? = null
    ) : DateFilterItem(type = TYPE_PER_MONTH) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}