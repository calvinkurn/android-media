package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.View
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils.outputFormat
import com.tokopedia.topads.dashboard.data.utils.Utils.stringToDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_dash_custom_date_picker_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Pika on 10/6/20.
 */

class CustomDatePicker : BottomSheetUnify() {

    lateinit var minDate: Date
    private lateinit var maxDate: Date
    private lateinit var selectedDate: Date
    var dateFlag = 0
    private lateinit var minDateOriginal: Date
    private lateinit var selectedDateOriginal: Date
    lateinit var selectedStartDate:Date


    private lateinit var listenerCalendar: ActionListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData()
        val childView = View.inflate(context, R.layout.topads_dash_custom_date_picker_sheet, null)
        setChild(childView)
    }

    private fun getBundleData() {
        arguments?.run {
            this.getString(MIN_DATE)?.let {
                minDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
                minDateOriginal = minDate
            }

            this.getString(MAX_DATE)?.let {
                maxDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
            }

            this.getString(SELECTED_DATE)?.let {
                selectedDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
                selectedStartDate = selectedDate
                selectedDateOriginal = selectedDate

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderSinglePickCalendar(arrayListOf())
        setFieldEnable(false)
        dateStart?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                dateFlag = 0
                selectedDate = selectedStartDate
                minDate = minDateOriginal
                renderSinglePickCalendar(arrayListOf())
            }
        }
        dateEnd?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if(dateStart.text.isNullOrEmpty()){
                    return@setOnFocusChangeListener
                }
                selectedDate = selectedDateOriginal
                dateFlag = 1
                renderSinglePickCalendar(arrayListOf())
            }
        }
        dateStart?.keyListener = null
        dateEnd?.keyListener = null
    }

    private fun setFieldEnable(enable: Boolean) {
        dateEnd?.isEnabled = enable
        dateStart?.isEnabled = enable
    }

    private fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        val calendar = calendarUnify.calendarPickerView
        calendar?.init(minDate, maxDate, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (dateFlag == 0) {
                    dateStart?.setText(outputFormat.format(date))
                    selectedStartDate = date
                    minDate = date
                    setFieldEnable(true)
                    dateEnd?.requestFocus()
                } else if (dateFlag == 1) {
                    dateEnd?.setText(outputFormat.format(date))
                    listenerCalendar.onCustomDateSelected(minDate,date)
                    GlobalScope.launch {
                        delay(300)
                        dismissAllowingStateLoss()
                    }
                }
            }

            override fun onDateUnselected(date: Date) {

            }
        })

    }

    fun setListener(listener: ActionListener) {
        this.listenerCalendar = listener
    }

    companion object {

        private const val MIN_DATE = "min_date"
        private const val MAX_DATE = "max_date"
        private const val SELECTED_DATE = "selected_date"
        const val TRAVEL_CAL_YYYY_MM_DD = "yyyy-MM-dd"


        fun getInstance(minDateString: String, maxDateString: String,
                        selectedDate: String): CustomDatePicker =
                CustomDatePicker().also {
                    it.arguments = Bundle().apply {
                        putString(MIN_DATE, minDateString)
                        putString(MAX_DATE, maxDateString)
                        putString(SELECTED_DATE, selectedDate)
                    }
                }
    }

    interface ActionListener {
        fun onCustomDateSelected(dateSelected: Date, endDate: Date)
    }
}