package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.view.customview.DateTextFieldView
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_calendar_picker.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class CalendarPicker(
        mContext: Context
) : BottomSheetUnify() {

    var selectedDates: List<Date> = emptyList()
        private set

    private val calendarView: CalendarPickerView?

    init {
        val child = View.inflate(mContext, R.layout.bottomsheet_stc_calendar_picker, null)
        calendarView = child.calendarPickerStc.calendarPickerView
        setChild(child)
        setupView(child)
        setTitle(mContext.getString(R.string.stc_select_date))
        setStyle(DialogFragment.STYLE_NORMAL, R.style.StcDialogStyle)
    }

    private fun setupView(child: View) = with(child) {
        edtStcDateStart.label = context.getString(R.string.stc_start_from)
        edtStcDateUntil.label = context.getString(R.string.stc_until)
        edtStcDateUntil.labelGravity = Gravity.END
    }

    fun init(): CalendarPicker {
        val days90 = 90L
        val days30 = 30L
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(days90))
        val maxDate = Date(DateTimeUtil.getNNextDaysTimestamp(days90))
        calendarView?.let { cpv ->
            cpv.init(minDate, maxDate, emptyList()).inMode(CalendarPickerView.SelectionMode.RANGE)
            cpv.scrollToDate(Date(DateTimeUtil.getNNextDaysTimestamp(days30)))
            cpv.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                override fun onDateSelected(date: Date) {
                    this@CalendarPicker.selectedDates = cpv.selectedDates

                    showSelectedDate(view?.edtStcDateStart, cpv.selectedDates.firstOrNull())
                    if (cpv.selectedDates.size > 1) {
                        showSelectedDate(view?.edtStcDateUntil, cpv.selectedDates.last())
                    } else {
                        showSelectedDate(view?.edtStcDateUntil, null)
                    }
                }

                override fun onDateUnselected(date: Date) {

                }
            })
        }
        return this
    }

    private fun showSelectedDate(edt: DateTextFieldView?, date: Date?) {
        edt?.run {
            if (null != date) {
                valueStr = DateTimeUtil.format(date.time, "dd MMM yyyy")
            } else {
                reset()
            }
        }
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }
}