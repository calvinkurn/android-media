package com.tokopedia.sellerorder.filter.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import timber.log.Timber
import java.util.*

class SomFilterDateBottomSheet(private var somListOrderParam: SomListGetOrderListParam, private var calenderFilterListener: CalenderListener): BottomSheetUnify() {

    companion object {
        const val START_DATE_INDEX = 1
        const val END_DATE_INDEX = 2
        const val PATTERN_DATE = "dd MMM yyyy"

        fun newInstance(somListOrderParam: SomListGetOrderListParam, calenderFilterListener: CalenderListener): SomFilterDateBottomSheet {
            return SomFilterDateBottomSheet(somListOrderParam, calenderFilterListener)
        }
    }

    var selectedDates: Map<Int, List<Date>> = mutableMapOf()

    private var calendarViewFilter: UnifyCalendar? = null
    private var tfStartDate: TextFieldUnify? = null
    private var tfEndDate: TextFieldUnify? = null
    private var calendarView: CalendarPickerView? = null
    private var btnSaveCalendar: UnifyButton? = null

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.RANGE
    private val minDate = Date(Utils.getNPastYearTimeStamp(2))
    private val maxDate = Date(Utils.getNNextDaysTimestamp(1L))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(requireContext(), R.layout.bottom_sheet_filter_date, null)
        calendarViewFilter = view.findViewById(R.id.somFilterCalendar)
        tfStartDate = view.findViewById(R.id.tfStartDate)
        tfEndDate = view.findViewById(R.id.tfEndDate)
        btnSaveCalendar = view.findViewById(R.id.btnSaveCalendar)
        calendarViewFilter?.calendarPickerView
        calendarView = calendarViewFilter?.calendarPickerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendarView()
        setDefaultSelectedDate()
        selectStartDate()
        selectEndDate()
        btnSaveCalendar()
    }

    private fun selectStartDate() {
        tfStartDate?.setOnClickListener {
            selectDateClickListener(START_DATE_INDEX)
        }
    }

    private fun selectEndDate() {
        tfEndDate?.setOnClickListener {
            selectDateClickListener(END_DATE_INDEX)
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
            showSelectedDate()
        }
    }

    private fun showSelectedDate() {
        tfStartDate?.textFieldInput?.setText(getSelectedDate(START_DATE_INDEX))
        tfEndDate?.textFieldInput?.setText(getSelectedDate(END_DATE_INDEX))
    }

    private fun selectDate(cpv: CalendarPickerView, date: Date, smoothScroll: Boolean = false) {
        try {
            cpv.selectDate(date, smoothScroll)
        } catch (e: IllegalArgumentException) {
            Timber.w(e)
        }
    }

    private fun setupCalendarView() {
        calendarView?.let { cpv ->
            cpv.init(minDate, maxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(maxDate)
            selectDateClickListener()
        }
    }

    private fun selectDateClickListener(index: Int = 0) {
        calendarView?.let { cpv ->
            cpv.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                override fun onDateSelected(date: Date) {
                    when (mode) {
                        CalendarPickerView.SelectionMode.RANGE -> {
                            this@SomFilterDateBottomSheet.selectedDates = mapOf(index to cpv.selectedDates)
                        }
                        else -> {}
                    }
                }

                override fun onDateUnselected(date: Date) {}
            })
        }
    }

    private fun getSelectedDate(index: Int): String {
        val date = selectedDates[index]?.firstOrNull()
        return Utils.format(date?.time ?: 0L, PATTERN_DATE)
    }

    private fun btnSaveCalendar() {
        btnSaveCalendar?.setOnClickListener {
            val startDate = getSelectedDate(START_DATE_INDEX)
            val endDate = getSelectedDate(END_DATE_INDEX)
            somListOrderParam.startDate = startDate
            somListOrderParam.endDate = endDate
            calenderFilterListener.onBtnSaveCalendarClicked(somListOrderParam)
            dismiss()
        }
    }

    interface CalenderListener {
        fun onBtnSaveCalendarClicked(somListOrderParam: SomListGetOrderListParam)
    }
}