package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.View
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils
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
    lateinit var selectedStartDate: Date
    private lateinit var listenerCalendar: ActionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData()
        val childView = View.inflate(context, R.layout.topads_dash_custom_date_picker_sheet, null)
        setChild(childView)
    }

    private fun getBundleData() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val selectDateDef: String = Utils.format.format(calendar.time)
        calendar.add(Calendar.YEAR, -1)
        val minDateDef = Utils.format.format(calendar.time)
        val maxDateDef: String = Utils.format.format(Date())
        minDate = minDateDef.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
        minDateOriginal = minDate
        maxDate = maxDateDef.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
        selectedDate = selectDateDef.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
        selectedStartDate = selectedDate
        selectedDateOriginal = selectedDate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(resources.getString(R.string.topads_dash_choose_date))
        renderSinglePickCalendar(arrayListOf())
        setFieldEnable(false)
        dateStart?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                dateFlag = 0
                selectedDate = selectedStartDate
                minDate = minDateOriginal
                renderSinglePickCalendar(arrayListOf())
            }
        }
        dateEnd?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                if (dateStart.textFieldInput.text.isNullOrEmpty()) {
                    return@setOnFocusChangeListener
                }
                selectedDate = minDate
                dateFlag = 1
                renderSinglePickCalendar(arrayListOf())
            }
        }
        dateStart?.textFieldInput?.keyListener = null
        dateEnd?.textFieldInput?.keyListener = null
    }

    private fun setFieldEnable(enable: Boolean) {
        dateEnd?.isEnabled = enable
        dateStart?.isEnabled = enable
    }

    private fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        if (minDate > maxDate) {
            maxDate = minDate
        }
        if (minDate > selectedDate || maxDate < selectedDate) {
            selectedDate = minDate
        }
        val calendar = calendarUnify.calendarPickerView
        calendar?.init(minDate, maxDate, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (dateFlag == 0) {
                    dateStart?.textFieldInput?.setText(outputFormat.format(date))
                    selectedStartDate = date
                    minDate = date
                    setFieldEnable(true)
                    dateEnd?.requestFocus()
                } else if (dateFlag == 1) {
                    dateEnd?.textFieldInput?.setText(outputFormat.format(date))
                    listenerCalendar.onCustomDateSelected(minDate, date)
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
        const val TRAVEL_CAL_YYYY_MM_DD = "yyyy-MM-dd"
        fun getInstance() = CustomDatePicker()
    }

    interface ActionListener {
        fun onCustomDateSelected(dateSelected: Date, endDate: Date)
    }
}