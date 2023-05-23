package com.tokopedia.review.feature.reputationhistory.view.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.tokopedia.datepicker.range.R
import com.tokopedia.datepicker.range.view.activity.DatePickerActivity
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.reputationhistory.util.DateHeaderFormatter
import com.tokopedia.review.feature.reputationhistory.view.activity.SellerReputationDatePickerActivity
import com.tokopedia.unifyprinciples.Typography
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by normansyahputa on 11/21/16.
 */
open class GMStatHeaderViewHelper(protected var itemView: View, private val isGmStat: Boolean) {

    @JvmField
    protected var calendarRange: Typography? = null
    @JvmField
    protected var calendarIcon: ImageView? = null
    private var monthNamesAbrev: Array<String>? = null
    private var gredyColor = 0
    private var greenColor = 0
    private var sDate: Long = 0
    private var eDate: Long = 0
    private var lastSelection = 0
    private var selectionType = DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
    private var isLoading = true

    private fun initView(itemView: View) {
        monthNamesAbrev = itemView.resources.getStringArray(R.array.lib_date_picker_month_entries)
        calendarRange =
            itemView.findViewById(com.tokopedia.review.R.id.calendar_range)
        calendarIcon =
            itemView.findViewById(com.tokopedia.review.R.id.calendar_icon)
        gredyColor = ResourcesCompat.getColor(
            itemView.resources,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400,
            null
        )
        greenColor = ResourcesCompat.getColor(
            itemView.resources,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500,
            null
        )
    }

    private fun resetToLoading() {
        isLoading = false
    }

    protected open fun setImageIcon() {
        calendarIcon?.loadImage(CALENDAR_IMAGE_URL)
    }

    fun stopLoading() {
        isLoading = true
    }

    /**
     * this is for [SellerReputationAdapter]
     *
     * @param dateHeaderFormatter
     * @param sDate
     * @param eDate
     * @param lastSelectionPeriod
     * @param selectionType
     */
    fun bindDate(
        dateHeaderFormatter: DateHeaderFormatter, sDate: Long, eDate: Long,
        lastSelectionPeriod: Int, selectionType: Int
    ) {
        this.sDate = sDate
        this.eDate = eDate
        this.lastSelection = lastSelectionPeriod
        this.selectionType = selectionType
        var startDate: String? = null
        if (sDate != -1L) {
            startDate = dateHeaderFormatter.getDateWithoutYearFormat(sDate)
        }
        var endDate: String? = null
        if (eDate != -1L) {
            endDate = dateHeaderFormatter.getEndDateFormat(eDate)
        }
        calendarRange?.text = StringBuilder("$startDate - $endDate")
        setImageIcon()
        stopLoading()
    }

    fun onClick(activity: Activity) {
        if (!isLoading || !isGmStat) {
            return
        }
        val intent = Intent(activity, DatePickerActivity::class.java)
        val maxCalendar = DateUtilHelper.maxCalendar
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        var minDate: Date? = Date()
        try {
            minDate = dateFormat.parse(MIN_DATE)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val minCalendar = Calendar.getInstance()
        minDate?.let {
            minCalendar.time = it
        }
        minCalendar[Calendar.HOUR_OF_DAY] = 0
        minCalendar[Calendar.MINUTE] = 0
        minCalendar[Calendar.SECOND] = 0
        minCalendar[Calendar.MILLISECOND] = 0
        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, sDate)
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, eDate)
        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE)
        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(activity))
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection)
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType)
        intent.putExtra(
            DatePickerConstant.EXTRA_PAGE_TITLE,
            activity.getString(com.tokopedia.review.R.string.reputation_history_set_date)
        )
        activity.startActivityForResult(intent, MOVE_TO_SET_DATE)
    }

    fun onClick(fragment: Fragment) {
        if (!isLoading || !isGmStat) {
            return
        }
        val intent = Intent(fragment.activity, SellerReputationDatePickerActivity::class.java)
        val maxCalendar = DateUtilHelper.maxCalendar
        val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        var minDate: Date? = Date()
        try {
            minDate = dateFormat.parse(MIN_DATE)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val minCalendar = Calendar.getInstance()
        minDate?.let {
            minCalendar.time = it
        }
        minCalendar[Calendar.HOUR_OF_DAY] = 0
        minCalendar[Calendar.MINUTE] = 0
        minCalendar[Calendar.SECOND] = 0
        minCalendar[Calendar.MILLISECOND] = 0
        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, sDate)
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, eDate)
        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE)
        intent.putExtra(
            DatePickerConstant.EXTRA_DATE_PERIOD_LIST,
            getPeriodRangeList(fragment.activity)
        )
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, lastSelection)
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, selectionType)
        intent.putExtra(
            DatePickerConstant.EXTRA_PAGE_TITLE,
            fragment.getString(com.tokopedia.review.R.string.reputation_history_set_date)
        )
        fragment.startActivityForResult(intent, MOVE_TO_SET_DATE)
    }

    private fun getPeriodRangeList(context: Context?): ArrayList<PeriodRangeModel> {
        val periodRangeList = ArrayList<PeriodRangeModel>()
        var startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()
        endCalendar.add(Calendar.DATE, -1)
        startCalendar.add(Calendar.DATE, -1)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis, startCalendar.timeInMillis, context?.getString(
                    com.tokopedia.datepicker.range.R.string.yesterday
                )
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = endCalendar.timeInMillis
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis, endCalendar.timeInMillis, context?.getString(
                    com.tokopedia.datepicker.range.R.string.seven_days_ago
                )
            )
        )
        startCalendar = Calendar.getInstance()
        startCalendar.timeInMillis = endCalendar.timeInMillis
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH)
        periodRangeList.add(
            PeriodRangeModel(
                startCalendar.timeInMillis, endCalendar.timeInMillis, context?.getString(
                    com.tokopedia.datepicker.range.R.string.thirty_days_ago
                )
            )
        )
        return periodRangeList
    }

    companion object {
        const val CALENDAR_IMAGE_URL =
            "https://images.tokopedia.net/img/android/review/review_calendar.png"
        const val MOVE_TO_SET_DATE = 1
        private const val MAX_DATE_RANGE = 60
        private const val MIN_DATE = "25/07/2015"
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }

    init {
        initView(itemView)
        resetToLoading()
    }
}