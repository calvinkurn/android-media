package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.os.Handler
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.utils.DateRangeFormatUtil
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

    var selectedDates: List<Date> = emptyList()
        private set

    private val calendarView: CalendarPickerView?

    init {
        val child = View.inflate(mContext, R.layout.bottomsheet_stc_calendar_picker, null)
        calendarView = child.calendarPickerStc.calendarPickerView
        setChild(child)
        setupView(child)
        isFullpage = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.StcDialogStyle)
    }

    private fun setupView(child: View) = with(child) {
        edtStcDate.label = context.getString(R.string.stc_date)
    }

    fun init(mode: CalendarPickerView.SelectionMode): CalendarPicker {
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
                    showSelectedDate(cpv.selectedDates, mode)
                }

                override fun onDateUnselected(date: Date) {

                }
            })
        }
        return this
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
        this.selectedDates = cpv.selectedDates
        Handler().postDelayed({
            dismiss()
        }, 500)
    }

    private fun showSelectedDate(dates: List<Date>, mode: CalendarPickerView.SelectionMode) {
        view?.edtStcDate?.run {
            val startDate = dates.firstOrNull()
            val endDate = dates.lastOrNull()
            if (startDate != null && endDate != null) {
                valueStr = if (mode == CalendarPickerView.SelectionMode.SINGLE) {
                    DateTimeUtil.format(startDate.time, "dd MMM yyyy")
                } else {
                    DateRangeFormatUtil.getDateRangeStr(startDate, endDate)
                }
            }
        }
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }
}