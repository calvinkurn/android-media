package com.tokopedia.topads.dashboard.data.utils

import android.content.Context

import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel
import com.tokopedia.topads.dashboard.R

import java.util.Calendar

object TopAdsDatePeriodUtil {

    fun getPeriodRangeList(context: Context): ArrayList<PeriodRangeModel> {
        val periodRangeList = ArrayList<PeriodRangeModel>()
        var startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        periodRangeList.add(PeriodRangeModel(endCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(R.string.label_today)))
        startCalendar.add(Calendar.DATE, -1)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, startCalendar.timeInMillis, context.getString(R.string.yesterday)))
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(R.string.seven_days_ago)))
        startCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(R.string.thirty_days_ago)))
        startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DATE, 1)
        periodRangeList.add(PeriodRangeModel(startCalendar.timeInMillis, endCalendar.timeInMillis, context.getString(R.string.label_this_month)))
        return periodRangeList
    }
}
