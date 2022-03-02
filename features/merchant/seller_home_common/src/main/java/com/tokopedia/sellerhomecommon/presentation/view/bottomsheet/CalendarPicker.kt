package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarPickerBinding
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class CalendarPicker : BaseBottomSheet<ShcCalendarPickerBinding>() {

    companion object {
        private const val KEY_FILTER_ITEM = "key_filter_item"
        private const val DEF_DATE_FORMAT = "dd/MM/yyyy"

        fun newInstance(filterItem: DateFilterItem.Pick?): CalendarPicker {
            return CalendarPicker().apply {
                isFullpage = true
                clearContentPadding = true
                filterItem?.let {
                    arguments = Bundle().apply {
                        putParcelable(KEY_FILTER_ITEM, it)
                    }
                }
            }
        }
    }

    var selectedDates: List<Date> = emptyList()

    private val filterItem: DateFilterItem.Pick? by lazy {
        arguments?.getParcelable(KEY_FILTER_ITEM)
    }
    private var mode: CalendarPickerView.SelectionMode = CalendarPickerView.SelectionMode.SINGLE
    private var calendarView: CalendarPickerView? = null
    private val minDate by lazy {
        filterItem?.calendarPickerMinDate
            ?: Date(DateTimeUtil.getNPastDaysTimestamp(ShcConst.INT_365.toLong()))
    }
    private val maxDate by lazy {
        filterItem?.calendarPickerMaxDate ?: Date()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcCalendarPickerBinding.inflate(inflater).apply {
            setChild(root)
            calendarView = calendarPickerStc.calendarPickerView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendarPickerView()
        setDefaultSelectedDate()
    }

    override fun setupView() = binding?.run {
        edtStcStartDate.label = getString(R.string.shc_start_from)
        edtStcEndDate.label = getString(R.string.shc_until)

        if (mode == CalendarPickerView.SelectionMode.SINGLE) {
            edtStcStartDate.label = getString(R.string.shc_date)
            edtStcEndDate.invisible()
        } else {
            edtStcEndDate.visible()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        showSelectedDate()
        super.onDismiss(dialog)
    }

    fun setMode(mode: CalendarPickerView.SelectionMode): CalendarPicker {
        this.mode = mode
        return this
    }

    fun showDatePicker(fm: FragmentManager, tag: String = CalendarPicker::class.java.simpleName) {
        show(fm, tag)
    }

    private fun setupCalendarPickerView() {
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
                    showFormattedDate(date)
                }

                override fun onDateUnselected(date: Date) {

                }
            })
        }
    }

    private fun showFormattedDate(date: Date) {
        showStartDate(date)
        clearEndDate()
    }

    private fun setDefaultSelectedDate() {
        calendarView?.let { cpv ->
            if (selectedDates.isEmpty()) {
                return
            }

            val startDate = selectedDates.firstOrNull()
            val endDate = selectedDates.lastOrNull()
            startDate?.let {
                selectDate(cpv, it)
            }
            endDate?.let {
                selectDate(cpv, it)
            }
            showSelectedDate()
        }
    }

    private fun selectDate(cpv: CalendarPickerView, date: Date, smoothScroll: Boolean = false) {
        try {
            cpv.selectDate(date, smoothScroll)
        } catch (e: IllegalArgumentException) {
            Timber.w(e)
        }
    }

    private fun selectDateRange(cpv: CalendarPickerView) {
        when (filterItem?.type) {
            DateFilterItem.TYPE_PER_WEEK -> {
                selectPerWeekDateRange(cpv)
            }
            DateFilterItem.TYPE_CUSTOM -> {
                selectCustomDateRange(cpv)
            }
            DateFilterItem.TYPE_CUSTOM_SAME_MONTH -> {
                selectCustomDateRangeSameMonth(cpv)
            }
        }
    }

    private fun selectCustomDateRangeSameMonth(cpv: CalendarPickerView) {
        if (cpv.selectedDates.isNotEmpty()) {
            binding?.tickerStcCalendarPicker?.visible()
            val selected: Date = cpv.selectedDates.first()
            if (cpv.selectedDates.size == ShcConst.INT_1) {
                updateMaxDateSameMonth(cpv, selected)
                selectDate(cpv, selected)

                val isMaxDate = getMaxDateStatus(selected)
                if (isMaxDate) {
                    this.selectedDates = cpv.selectedDates
                    dismiss()
                }
            } else {
                this.selectedDates = cpv.selectedDates
                dismiss()
            }
        }
    }

    private fun getMaxDateStatus(selected: Date): Boolean {
        val maxMonth = Calendar.getInstance(Locale.getDefault())
        maxMonth.time = selected
        maxMonth.set(Calendar.DAY_OF_MONTH, maxMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        val isMaxMonth = selected == maxMonth.time

        val maxDate: Date = if (maxMonth.time.time > maxDate.time) {
            maxDate
        } else {
            maxMonth.time
        }
        val maxDateStr = DateTimeUtil.format(maxDate.time, ShcConst.FORMAT_DD_MM_YYYY)
        val selectedStr = DateTimeUtil.format(selected.time, ShcConst.FORMAT_DD_MM_YYYY)
        return isMaxMonth || maxDateStr == selectedStr
    }

    private fun updateMaxDateSameMonth(cpv: CalendarPickerView, selected: Date) {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal.time = selected
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        val maxDate: Date = if (cal.time.time > maxDate.time) {
            maxDate
        } else {
            cal.time
        }
        cpv.init(selected, maxDate, emptyList()).inMode(mode)
    }

    private fun selectCustomDateRange(cpv: CalendarPickerView) {
        val isDateRangeSelected = cpv.selectedDates.size > ShcConst.INT_1
        if (isDateRangeSelected) {
            this.selectedDates = cpv.selectedDates
            dismiss()
        } else {
            if (cpv.selectedDates.isNotEmpty()) {
                val newMaxDateMillis = TimeUnit.DAYS.toMillis(ShcConst.INT_90.toLong())
                val selectedDate = cpv.selectedDates.first()
                val areDatesSame = getAreDatesSame(selectedDate, maxDate)

                if (areDatesSame) {
                    this.selectedDates = listOf(selectedDate, selectedDate)
                    dismiss()
                }

                val tmpNewMaxDate = selectedDate.time.plus(newMaxDateMillis)
                val newMaxDate = if (tmpNewMaxDate > maxDate.time) {
                    maxDate
                } else {
                    Date(tmpNewMaxDate)
                }
                cpv.init(selectedDate, newMaxDate, emptyList()).inMode(mode)
                selectDate(cpv, selectedDate)
            }
        }
    }

    private fun getAreDatesSame(date: Date, otherDate: Date): Boolean {
        val dateFmt = DateFormatUtils.getFormattedDate(date.time, DEF_DATE_FORMAT)
        val otherDateFmt = DateFormatUtils.getFormattedDate(otherDate.time, DEF_DATE_FORMAT)
        return dateFmt == otherDateFmt
    }

    private fun selectPerWeekDateRange(cpv: CalendarPickerView) {
        if (cpv.selectedDates.isNotEmpty()) {
            val selected: Date = cpv.selectedDates.first()
            val selectedPair = getPerWeekSelectedPair(selected)

            try {
                selectDate(cpv, selected)
                selectDate(cpv, selectedPair.first)
                selectDate(cpv, selectedPair.second)
            } catch (e: IllegalArgumentException) {
                val m6Days = TimeUnit.DAYS.toMillis(ShcConst.INT_6.toLong())
                val minDateMillis = minDate.time.plus(m6Days)
                val mSelectedPair = if (minDateMillis >= selected.time) {
                    getPerWeekSelectedPair(Date(minDateMillis))
                } else {
                    val maxDateMillis = maxDate.time.minus(m6Days)
                    getPerWeekSelectedPair(Date(maxDateMillis))
                }
                selectDate(cpv, mSelectedPair.first)
                selectDate(cpv, mSelectedPair.second, true)
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

        val m6Day = TimeUnit.DAYS.toMillis(ShcConst.INT_6.toLong())
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = selected
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val firstDateOfWeek = if (calendar.time.time < minDate.time) {
            minDate
        } else {
            calendar.time
        }
        val lastDateOfWeek = Date(calendar.time.time + m6Day)
        return Pair(firstDateOfWeek, lastDateOfWeek)
    }

    private fun onGoingWeekPair(selected: Date): Pair<Date, Date>? {
        val firstDayOfOnGoingWeek: Calendar = Calendar.getInstance()
        with(firstDayOfOnGoingWeek) {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
            set(Calendar.MINUTE, ShcConst.INT_0)
            set(Calendar.SECOND, ShcConst.INT_0)
            set(Calendar.MILLISECOND, ShcConst.INT_0)
        }

        val firstDayMillis = firstDayOfOnGoingWeek.timeInMillis
        val currentDate = Date()
        val canSelectOnGoingWeek = selected.time in firstDayMillis..currentDate.time

        val isTodayIsMaxDate = DateTimeUtil.format(
            currentDate.time,
            DateTimeUtil.FORMAT_DD_MM_YYYY
        ) == DateTimeUtil.format(maxDate.time, DateTimeUtil.FORMAT_DD_MM_YYYY)

        return if (canSelectOnGoingWeek && isTodayIsMaxDate) {
            Pair(firstDayOfOnGoingWeek.time, currentDate)
        } else {
            null
        }
    }

    private fun showSelectedDate() {
        val startDate = selectedDates.firstOrNull()
        startDate?.let {
            showStartDate(it)
        }
        val endDate = selectedDates.lastOrNull()
        endDate?.let {
            showEndDate(it)
        }
    }

    private fun showStartDate(date: Date) {
        val formattedDate = DateTimeUtil.format(date.time, ShcConst.FORMAT_DD_MM_YYYY)
        binding?.edtStcStartDate?.valueStr = formattedDate
    }

    private fun showEndDate(date: Date) {
        val formattedDate = DateTimeUtil.format(date.time, ShcConst.FORMAT_DD_MM_YYYY)
        binding?.edtStcEndDate?.valueStr = formattedDate
    }

    private fun clearEndDate() {
        binding?.edtStcEndDate?.run {
            valueStr = ""
            hint = hint
        }
    }
}