package com.tokopedia.statistic.presentation.view.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_calendar_picker.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class CalendarPicker(
        mContext: Context
) : BottomSheetUnify() {

    private val calendarView: CalendarPickerView?

    init {
        val child = View.inflate(mContext, R.layout.bottomsheet_stc_calendar_picker, null)
        calendarView = child.calendarPickerStc.calendarPickerView
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    fun init(): CalendarPicker {
        val days90 = 90L
        val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(days90))
        val maxDate = Date(DateTimeUtil.getNNextDaysTimestamp(days90))
        calendarView?.init(minDate, maxDate, emptyList())?.inMode(CalendarPickerView.SelectionMode.RANGE)
        return this
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }
}