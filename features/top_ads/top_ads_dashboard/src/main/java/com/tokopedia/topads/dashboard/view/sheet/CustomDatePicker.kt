package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.View
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.data.utils.Utils.outputFormat
import com.tokopedia.topads.dashboard.data.utils.Utils.stringToDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Pika on 10/6/20.
 */

private const val SIX_MONTH = -6
private const val ONE_UNIT = -1
private const val DELAY_TIME = 300L

class CustomDatePicker : BottomSheetUnify() {

    private var dateStart: TextFieldUnify? = null
    private var dateEnd: TextFieldUnify? = null
    private var calendarUnify: UnifyCalendar? = null

    private lateinit var minDate: Date
    private lateinit var maxDate: Date
    private lateinit var selectedDate: Date
    private var dateFlag = 0
    private lateinit var minDateOriginal: Date
    private lateinit var selectedDateOriginal: Date
    private lateinit var selectedStartDate: Date
    private lateinit var listenerCalendar: ActionListener
    private var isCreditSheet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData()
        val childView = View.inflate(context, R.layout.topads_dash_custom_date_picker_sheet, null)
        dateStart = childView.findViewById(R.id.dateStart)
        dateEnd = childView.findViewById(R.id.dateEnd)
        calendarUnify = childView.findViewById(R.id.calendarUnify)
        setChild(childView)
    }

    private fun getBundleData() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, ONE_UNIT)
        val selectDateDef: String = Utils.format.format(calendar.time)
        if (isCreditSheet) {
            calendar.add(Calendar.MONTH, SIX_MONTH)
        } else {
            calendar.add(Calendar.YEAR, ONE_UNIT)
        }
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
                if (dateStart?.textFieldInput?.text.isNullOrEmpty()) {
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

    private fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        if (minDate > maxDate) {
            maxDate = minDate
        }
        if (minDate > selectedDate || maxDate < selectedDate) {
            selectedDate = minDate
        }
        val calendar = calendarUnify?.calendarPickerView
        calendar?.init(minDate, maxDate, holidayArrayList)
            ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
            ?.withSelectedDate(selectedDate)

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (dateFlag == 0) {
                    dateStart?.textFieldInput?.setText(outputFormat.format(date))
                    selectedStartDate = date
                    minDate = date
                    dateEnd?.requestFocus()
                } else if (dateFlag == 1) {
                    dateEnd?.textFieldInput?.setText(outputFormat.format(date))
                    listenerCalendar.onCustomDateSelected(minDate, date)
                    GlobalScope.launch {
                        delay(DELAY_TIME)
                        dismissAllowingStateLoss()
                    }
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    fun setListener(listener: ActionListener) {
        this.listenerCalendar = listener
    }

    fun setCreditSheet() {
        isCreditSheet = true
    }

    companion object {
        const val TRAVEL_CAL_YYYY_MM_DD = "yyyy-MM-dd"
        fun getInstance() = CustomDatePicker()
    }

    interface ActionListener {
        fun onCustomDateSelected(dateSelected: Date, endDate: Date)
    }
}