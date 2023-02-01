package com.tokopedia.mvc.presentation.bottomsheet.editperiod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodCalendarBinding
import com.tokopedia.mvc.presentation.quota.QuotaInfoBottomSheet
import com.tokopedia.mvc.util.getToday
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.SimpleDateFormat
import java.util.*

class VoucherEditCalendarBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodCalendarBinding>()

    private var calendar: CalendarPickerView? = null

    private var startDate: GregorianCalendar? = null
    private var endDate: GregorianCalendar? = null
    private var startCalendar: GregorianCalendar? = null
    private var hour: Int = 0
    private var minute: Int = 0
    private var callback: (Calendar) -> Unit = {}

    private var timePicker: DateTimePickerUnify? = null
    private val dateFormat = SimpleDateFormat("d MMMM", LocaleUtils.getIDLocale())

    init {
        isFullpage = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetEditPeriodCalendarBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(
            context?.resources?.getString(R.string.edit_period_calender_title).toBlankOrString()
        )
        setAction(context?.getString(R.string.edit_period_see_remaining_quota).toBlankOrString()) {
            showQuotaInfoBottomSheet()
        }
        dateFormat.timeZone = TimeZone.getDefault()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun showQuotaInfoBottomSheet() {
        val bottomSheet = QuotaInfoBottomSheet()
        bottomSheet.show(childFragmentManager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = binding?.voucherCreationCalendar?.calendarPickerView
        renderCalendar(arrayListOf())

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                setUpTimePickerBottomSheet(date)
            }

            override fun onDateUnselected(date: Date) {
            }
        })
    }

    var listener = object : OnDateChangedListener {
        override fun onDateChanged(date: Long) {}
    }
    private fun setUpTimePickerBottomSheet(selectedDate: Date) {
        val pickerStartTime = GregorianCalendar()
        val pickerEndTime = GregorianCalendar()

        val selectedDateGregorian = GregorianCalendar(
            context?.let {
                LocaleUtils.getIDLocale()
            }
        )
        selectedDateGregorian.time = selectedDate
        selectedDateGregorian.apply {
            val date1 = context?.getToday()?.get(Calendar.DATE)
            val date2 = selectedDateGregorian.get(Calendar.DATE)
            val month1 = context?.getToday()?.get(Calendar.MONTH)
            val month2 = selectedDateGregorian.get(Calendar.MONTH)

            if (date1 == date2 && month1 == month2) {
                pickerStartTime.apply {
                    add(Calendar.HOUR_OF_DAY, ADD_3_HOURS)
                }
            } else {
                pickerStartTime.apply {
                    set(Calendar.HOUR_OF_DAY, MIN_TIME_OF_DAY)
                    set(Calendar.MINUTE, MIN_TIME_OF_DAY)
                }
            }

            val currentDate = GregorianCalendar(
                context?.let {
                    LocaleUtils.getIDLocale()
                }
            )
            currentDate.time = selectedDate
            val defaultDate = currentDate.apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            pickerEndTime.time = selectedDate
            pickerEndTime.apply {
                set(Calendar.HOUR_OF_DAY, MAX_HOUR_OF_DAY)
                set(Calendar.MINUTE, MAX_MINUTE_OF_DAY)
            }
            initTimePicker(defaultDate, pickerStartTime, pickerEndTime)
            initTitleForTimePicker(selectedDate)

            timePicker?.show(
                childFragmentManager,
                context?.resources?.getString(R.string.edit_period_calendar_show_tag)
                    .toBlankOrString()
            )
        }
    }

    private fun initTimePicker(
        defaultDate: GregorianCalendar,
        start: GregorianCalendar?,
        end: GregorianCalendar?
    ) {
        timePicker = context?.let { timePickerContext ->
            start?.let { start ->
                end?.let { end ->
                    DateTimePickerUnify(
                        timePickerContext,
                        start,
                        defaultDate,
                        end,
                        listener,
                        DateTimePickerUnify.TYPE_TIMEPICKER
                    )
                }
            }
        }
    }

    private fun initTitleForTimePicker(selectedDate: Date) {
        timePicker?.apply {
            hourInterval = HOUR_INTERVAL
            minuteInterval = MINUTE_INTERVAL

            setTitle(
                this@VoucherEditCalendarBottomSheet.getString(
                    R.string.edit_period_time_picker_title
                )
                    .toBlankOrString()
            )
            setInfo(
                "${
                this@VoucherEditCalendarBottomSheet.getString(R.string.edit_period_calender_title)
                    .toBlankOrString()
                }: ${dateFormat.format(selectedDate)}"
            )
            setInfoVisible(true)
            datePickerButton.text = this@VoucherEditCalendarBottomSheet.getString(R.string.smvc_select).toBlankOrString()
            datePickerButton.setOnClickListener {
                getDate()
                callback.invoke(getDate())
                dismiss()
                this@VoucherEditCalendarBottomSheet.dismiss()
            }
        }
    }

    private fun renderCalendar(holidayArrayList: ArrayList<Legend>) {
        var selectedDates = startCalendar?.let {
            arrayListOf(it.time)
        }

        if (!startCalendar?.compareTo(endDate).isLessThanZero()) {
            selectedDates = endDate?.let {
                arrayListOf(it.time)
            }
        }

        startDate?.time?.let {
            endDate?.time?.let { it1 ->
                calendar?.init(it, it1, holidayArrayList)
                    ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                    ?.withSelectedDates(selectedDates)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            startCalendar: GregorianCalendar?,
            minDate: GregorianCalendar,
            maxDate: GregorianCalendar,
            hour: Int,
            minute: Int,
            callback: (Calendar) -> Unit
        ): VoucherEditCalendarBottomSheet {
            return VoucherEditCalendarBottomSheet().apply {
                this.startDate = minDate
                this.endDate = maxDate
                this.startCalendar = startCalendar
                this.hour = hour
                this.minute = minute
                this.callback = callback
            }
        }

        private const val MAX_HOUR_OF_DAY = 23
        private const val MAX_MINUTE_OF_DAY = 59
        private const val MIN_TIME_OF_DAY = 0
        private const val HOUR_INTERVAL = 1
        private const val MINUTE_INTERVAL = 30
        private const val ADD_3_HOURS = 3
    }
}
