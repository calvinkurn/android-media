package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.DateRangeFormatUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_calendar_picker.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class CalendarPicker(
        private val mContext: Context
) : BottomSheetUnify() {

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.SINGLE
    var selectedDates: List<Date> = emptyList()

    private val calendarView: CalendarPickerView?

    init {
        val child = View.inflate(mContext, R.layout.bottomsheet_stc_calendar_picker, null)
        calendarView = child.calendarPickerStc.calendarPickerView
        setChild(child)
        setupView(child)
        isFullpage = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.StcDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultSelectedDate()
    }

    fun init(mode: CalendarPickerView.SelectionMode): CalendarPicker {
        this.mode = mode
        val days90 = 90L
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(days90))
        val maxDate = Date(DateTimeUtil.getNNextDaysTimestamp(1))
        calendarView?.let { cpv ->
            cpv.init(minDate, maxDate, emptyList()).inMode(mode)
            cpv.scrollToDate(maxDate)
            cpv.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                override fun onDateSelected(date: Date) {
                    when (mode) {
                        CalendarPickerView.SelectionMode.SINGLE -> {
                            this@CalendarPicker.selectedDates = cpv.selectedDates
                            dismiss()
                        }
                        else -> selectDateRange(cpv)
                    }
                    showSelectedDate()
                }

                override fun onDateUnselected(date: Date) {

                }
            })
        }
        return this
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }

    private fun setupView(child: View) = with(child) {
        edtStcDate.label = context.getString(R.string.stc_date)
    }

    private fun setDefaultSelectedDate() {
        calendarView?.let { cpv ->
            val startDate = selectedDates.firstOrNull()
            val endDate = selectedDates.lastOrNull()
            startDate?.let {
                cpv.selectDate(it)
            }
            endDate?.let {
                cpv.selectDate(it)
            }
            showSelectedDate()
        }
    }

    private fun selectDateRange(cpv: CalendarPickerView) {
        if (cpv.selectedDates.isNotEmpty()) {
            val selected: Date = cpv.selectedDates.first()
            val nextSixDays = Date(selected.time.plus(TimeUnit.DAYS.toMillis(6)))

            try {
                cpv.selectDate(nextSixDays)
            } catch (e: IllegalArgumentException) {
                val today = Date()
                val sixDaysBefore = Date(today.time.minus(TimeUnit.DAYS.toMillis(6)))
                cpv.selectDate(sixDaysBefore)
                cpv.selectDate(today, true)
            }
        }
        DateTimeUtil
        this.selectedDates = cpv.selectedDates
        dismiss()
    }

    private fun showSelectedDate() {
        view?.edtStcDate?.run {
            val startDate = selectedDates.firstOrNull()
            val endDate = selectedDates.lastOrNull()
            if (startDate != null && endDate != null) {
                valueStr = if (mode == CalendarPickerView.SelectionMode.SINGLE) {
                    DateTimeUtil.format(startDate.time, "dd MMM yyyy")
                } else {
                    DateRangeFormatUtil.getDateRangeStr(startDate, endDate)
                }
            }
        }
    }
}