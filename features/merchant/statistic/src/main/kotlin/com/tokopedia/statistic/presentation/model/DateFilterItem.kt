package com.tokopedia.statistic.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.statistic.presentation.view.adapter.factory.DateFilterAdapterFactory
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
) : Visitable<DateFilterAdapterFactory> {

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

    fun getHeaderSubTitle(): String {
        when (type) {
            TYPE_TODAY -> {
                val startDateMillis = startDate?.time ?: return ""
                val dateStr = DateTimeUtil.format(startDateMillis, "dd MMMM")
                val hourStr = DateTimeUtil.format(System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)), "HH:00")
                return "Hari Ini ($dateStr 00:00 - $hourStr)"
            }
            TYPE_LAST_7_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return "7 Hari Terakhir ($dateRangeStr)"
            }
            TYPE_LAST_30_DAYS -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return "30 Hari Terakhir ($dateRangeStr)"
            }
            TYPE_PER_DAY -> {
                val startDateMillis = startDate?.time ?: return ""
                return "Per Hari (${DateTimeUtil.format(startDateMillis, "dd MMM yyyy")})"
            }
            TYPE_PER_WEEK -> {
                val mStartDate = startDate ?: return ""
                val mEndDate = endDate ?: return ""
                val dateRangeStr = DateFilterFormatUtil.getDateRangeStr(mStartDate, mEndDate)
                return "Per Minggu ($dateRangeStr)"
            }
            TYPE_PER_MONTH -> {
                startDate?.let {
                    val monthFmt = DateTimeUtil.format(it.time, "MMMM yyyy")
                    return "Per Bulan ($monthFmt)"
                }
                return "Per Bulan"
            }
        }
        return ""
    }

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

    data class Pick(
            override val label: String,
            override var startDate: Date? = null,
            override var endDate: Date? = null,
            override var isSelected: Boolean = false,
            override val type: Int
    ) : DateFilterItem(label, startDate, endDate, isSelected, type) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object ApplyButton : DateFilterItem(type = TYPE_BUTTON) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object Divider : DateFilterItem(type = TYPE_DIVIDER) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class MonthPickerItem(
            override val label: String,
            override var startDate: Date? = null,
            override var endDate: Date? = null,
            override var isSelected: Boolean = false
    ) : DateFilterItem(type = TYPE_PER_MONTH) {

        override fun type(typeFactory: DateFilterAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}