package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import timber.log.Timber
import java.util.*

class SomFilterDateBottomSheet : BottomSheetUnify() {

    companion object {
        const val START_DATE_INDEX = 1
        const val END_DATE_INDEX = 2
        const val PATTERN_DATE = "dd MMM yyyy"
        const val TITLE_FILTER_DATE = "Pilih Tanggal"

        fun newInstance(): SomFilterDateBottomSheet {
            return SomFilterDateBottomSheet()
        }
    }

    var selectedDates: Map<Int, List<Date>> = mutableMapOf()

    private var calendarViewFilter: UnifyCalendar? = null
    private var tfStartDate: TextFieldUnify? = null
    private var tfEndDate: TextFieldUnify? = null
    private var calendarView: CalendarPickerView? = null
    private var btnSaveCalendar: UnifyButton? = null

    private var calenderFilterListener: CalenderListener? = null

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filter_date, container, false)
        setChildView(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendarView()
        setDefaultSelectedDate()
        selectStartDate()
        selectEndDate()
        selectDateClickListener()
        btnSaveCalendar()
    }

    private fun setChildView(view: View) {
        calendarViewFilter = view.findViewById(R.id.somFilterCalendar)
        tfStartDate = view.findViewById(R.id.tfStartDate)
        tfEndDate = view.findViewById(R.id.tfEndDate)
        btnSaveCalendar = view.findViewById(R.id.btnSaveCalendar)
        calendarView = calendarViewFilter?.calendarPickerView
        setChild(view)
        setTitle(TITLE_FILTER_DATE)
        isFullpage = true
    }

    private fun selectStartDate() {
        tfStartDate?.textFieldInput?.setOnClickListener {
            selectDateClickListener()
        }
    }

    private fun selectEndDate() {
        tfEndDate?.textFieldInput?.setOnClickListener {
            selectDateClickListener()
        }
    }

    private fun setDefaultSelectedDate() {
        calendarView?.let { cpv ->
            val startDate = selectedDates[START_DATE_INDEX]?.firstOrNull()
            val endDate = selectedDates[END_DATE_INDEX]?.firstOrNull()
            startDate?.let {
                selectDate(cpv, it)
            }
            endDate?.let {
                selectDate(cpv, it)
            }
        }
    }

    private fun selectDate(cpv: CalendarPickerView, date: Date, smoothScroll: Boolean = false) {
        try {
            cpv.selectDate(date, smoothScroll)
        } catch (e: IllegalArgumentException) {
            Timber.w(e)
        }
    }

    private fun setupCalendarView() {
        val minDate = Date(Utils.getNPastYearTimeStamp(2))
        val maxDate = Date(Utils.getNNextDaysTimestamp(1L))

        calendarView?.let { cpv ->
            cpv.init(minDate, maxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(maxDate)
            selectDateClickListener()
        }
    }

    private fun selectDateClickListener() {
        calendarView?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            var dateIn: Date? = null
            override fun onDateSelected(date: Date) {
                when (mode) {
                    CalendarPickerView.SelectionMode.RANGE -> {
                        if(dateIn != null && date.after(dateIn) && tfStartDate?.textFieldInput?.isFocused == true) {
                            tfEndDate?.textFieldInput?.setText(getSelectedDate(date))
                            tfEndDate?.textFieldInput?.requestFocus()
                        } else {
                            dateIn = date
                            tfStartDate?.textFieldInput?.setText("")
                            tfStartDate?.textFieldInput?.setText(getSelectedDate(date))
                            tfStartDate?.textFieldInput?.requestFocus()                            }
                    }
                    else -> { }
                }
            }

            override fun onDateUnselected(date: Date) {}
        })
    }

    private fun getSelectedDate(date: Date): String {
        return Utils.format(date.time, PATTERN_DATE)
    }

    private fun btnSaveCalendar() {
        btnSaveCalendar?.setOnClickListener {
            val startDate = tfStartDate?.textFieldInput?.text?.trim().toString()
            val endDate = tfEndDate?.textFieldInput?.text?.trim().toString()
            calenderFilterListener?.onBtnSaveCalendarClicked(startDate, endDate)
            dismiss()
        }
    }

    fun setCalendarListener(calenderFilterListener: CalenderListener) {
        this.calenderFilterListener = calenderFilterListener
    }

    interface CalenderListener {
        fun onBtnSaveCalendarClicked(startDate: String, endDate: String)
    }
}