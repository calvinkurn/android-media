package com.tokopedia.statistic.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_stc_calendar_picker.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class CalendarPicker : BottomSheetUnify() {

    companion object {
        fun newInstance(): CalendarPicker {
            return CalendarPicker().apply {
                isFullpage = true
                clearContentPadding = true
                setStyle(DialogFragment.STYLE_NORMAL, R.style.StcDialogStyle)
            }
        }
    }

    var selectedDates: List<Date> = emptyList()

    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.SINGLE
    private var calendarView: CalendarPickerView? = null
    private val minDate = Date(DateTimeUtil.getNPastDaysTimestamp(90L))
    private val maxDate = Date(DateTimeUtil.getNNextDaysTimestamp(1L))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setChild(inflater: LayoutInflater, container: ViewGroup?) {
        val child = inflater.inflate(R.layout.bottomsheet_stc_calendar_picker, container, false)
        child.calendarPickerStc.calendarPickerView
        setChild(child)
        calendarView = child.calendarPickerStc.calendarPickerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setDefaultSelectedDate()
    }

    fun setMode(mode: CalendarPickerView.SelectionMode): CalendarPicker {
        this.mode = mode
        return this
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }

    private fun setupView() = view?.run {
        edtStcDate.label = context.getString(R.string.stc_date)

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
            val selectedPair = getPerWeekSelectedPair(selected)

            try {
                cpv.selectDate(selected)
                cpv.selectDate(selectedPair.first)
                cpv.selectDate(selectedPair.second)
            } catch (e: IllegalArgumentException) {
                val m6Days = TimeUnit.DAYS.toMillis(6)
                val minDateMillis = minDate.time.plus(m6Days)
                val mSelectedPair = if (minDateMillis >= selected.time) {
                    getPerWeekSelectedPair(Date(minDateMillis))
                } else {
                    val maxDateMillis = maxDate.time.minus(m6Days)
                    getPerWeekSelectedPair(Date(maxDateMillis))
                }
                cpv.selectDate(mSelectedPair.first)
                cpv.selectDate(mSelectedPair.second, true)
            }
        }
        this.selectedDates = cpv.selectedDates
        dismiss()
    }

    private fun getPerWeekSelectedPair(selected: Date): Pair<Date, Date> {
        val onGoingWeekPair = onGoingWeekPair(selected)
        if (onGoingWeekPair != null) {
            return onGoingWeekPair
        }

        val m6Day = TimeUnit.DAYS.toMillis(6)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = selected
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val firstDateOfWeek = calendar.time
        val lastDateOfWeek = Date(firstDateOfWeek.time + m6Day)
        return Pair(firstDateOfWeek, lastDateOfWeek)
    }

    private fun onGoingWeekPair(selected: Date): Pair<Date, Date>? {
        val firstDayOfOnGoingWeek: Calendar = Calendar.getInstance()
        with(firstDayOfOnGoingWeek) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val firstDayMillis = firstDayOfOnGoingWeek.timeInMillis
        val currentDate = Date()
        val canSelectOnGoingWeek = selected.time in firstDayMillis..currentDate.time

        return if (canSelectOnGoingWeek) {
            Pair(firstDayOfOnGoingWeek.time, currentDate)
        } else {
            null
        }
    }

    private fun showSelectedDate() {
        view?.edtStcDate?.run {
            val startDate = selectedDates.firstOrNull()
            val endDate = selectedDates.lastOrNull()
            if (startDate != null && endDate != null) {
                valueStr = if (mode == CalendarPickerView.SelectionMode.SINGLE) {
                    DateTimeUtil.format(startDate.time, "dd MMM yyyy")
                } else {
                    DateFilterFormatUtil.getDateRangeStr(startDate, endDate)
                }
            }
        }
    }
}