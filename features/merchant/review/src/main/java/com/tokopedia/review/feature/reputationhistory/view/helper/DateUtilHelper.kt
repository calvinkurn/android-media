package com.tokopedia.review.feature.reputationhistory.view.helper

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel
import com.tokopedia.review.R
import com.tokopedia.review.feature.reputationhistory.view.activity.SellerReputationDatePickerActivity
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtilHelper {

    private var sDate: Long = 0
    private var eDate: Long = 0

    fun onClick(fragment: Fragment) {
        val intent = Intent(fragment.activity, SellerReputationDatePickerActivity::class.java)
        val maxCalendar = maxCalendar
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        var minDate = Date()
        try {
            minDate = dateFormat?.parse(MIN_DATE)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val minCalendar = getMinCalendar(minDate)
        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, sDate)
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, eDate)
        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE)
        intent.putExtra(
            DatePickerConstant.EXTRA_DATE_PERIOD_LIST,
            getPeriodRangeList(fragment.activity)
        )
        intent.putExtra(
            DatePickerConstant.EXTRA_SELECTION_TYPE,
            DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE
        )
        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, fragment.getString(R.string.reputation_history_set_date))
        fragment.startActivityForResult(intent, MOVE_TO_SET_DATE)
    }

    private fun getMinCalendar(minDate: Date): Calendar {
        val minCalendar = Calendar.getInstance()
        minCalendar.time = minDate
        minCalendar[Calendar.HOUR_OF_DAY] = 0
        minCalendar[Calendar.MINUTE] = 0
        minCalendar[Calendar.SECOND] = 0
        minCalendar[Calendar.MILLISECOND] = 0
        return minCalendar
    }

    private fun getPeriodRangeList(context: Context?): ArrayList<PeriodRangeModel> {
        val periodRangeList = ArrayList<PeriodRangeModel>()
        var startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        startCalendar.add(Calendar.DATE, -1)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                startCalendar.timeInMillis,
                context?.getString(com.tokopedia.datepicker.range.R.string.yesterday)
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = endCalendar.timeInMillis
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context?.getString(com.tokopedia.datepicker.range.R.string.seven_days_ago)
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = endCalendar.timeInMillis
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis,
                endCalendar.timeInMillis,
                context?.getString(com.tokopedia.datepicker.range.R.string.thirty_days_ago)
            )
        )
        return periodRangeList
    }

    fun setsDate(sDate: Long) {
        this.sDate = sDate
    }

    fun seteDate(eDate: Long) {
        this.eDate = eDate
    }

    companion object {
        const val MOVE_TO_SET_DATE = 1
        private const val MAX_DATE_RANGE = 60
        private const val MIN_DATE = "25/07/2015"
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val MAX_HOUR_OF_DAY = 23
        private const val MAX_MINUTE_CALENDAR = 59
        private const val MAX_SECOND_CALENDAR = 59

        val maxCalendar: Calendar
            get() {
                val maxCalendar = Calendar.getInstance()
                maxCalendar[Calendar.HOUR_OF_DAY] = MAX_HOUR_OF_DAY
                maxCalendar[Calendar.MINUTE] = MAX_MINUTE_CALENDAR
                maxCalendar[Calendar.SECOND] = MAX_SECOND_CALENDAR
                return maxCalendar
            }
    }
}