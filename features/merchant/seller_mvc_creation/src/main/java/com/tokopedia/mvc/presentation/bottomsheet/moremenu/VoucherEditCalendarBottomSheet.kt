package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodCalendarBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.text.SimpleDateFormat
import java.util.*

class VoucherEditCalendarBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodCalendarBinding>()

    var calendar: CalendarPickerView? = null

    private var startDate: GregorianCalendar? = null
    private var endDate: GregorianCalendar? = null
    private var startCalendar: GregorianCalendar? = null
    private var hour: Int = 0
    private var minute: Int = 0

    private var timePicker: DateTimePickerUnify? = null
    val locale = Locale("id", "ID")
    val dateFormat = SimpleDateFormat("d MMMM", locale)

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
        setTitle(context?.getString(R.string.edit_period_calender_title).toBlankOrString())
        dateFormat.timeZone = TimeZone.getDefault()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = binding?.voucherCreationCalendar?.calendarPickerView
        renderCalendar(arrayListOf())

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                // put your implementation here
                // Save the currentDate to ViewModel
                // Navigate To Time Picker Bottom Sheet
                val selectedDate = dateFormat.format(date)
                showTimePickerBottomSheet(selectedDate)
            }

            override fun onDateUnselected(date: Date) {
            }
        })
    }

    var listener = object : OnDateChangedListener {
        override fun onDateChanged(date: Long) {
            var calendarDate = GregorianCalendar.getInstance()
            calendarDate.timeInMillis = date
            Log.d("DatePicker listener", "${calendarDate.time}")
        }
    }

    private fun showTimePickerBottomSheet(selectedDate: String) {
        var minTime = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) }).apply {
            set(Calendar.HOUR_OF_DAY, MIN_TIME_OF_DAY)
            set(Calendar.MINUTE, MIN_TIME_OF_DAY)
        }
        //     var defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(this))
        var maxTime = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) }).apply {
            set(Calendar.HOUR_OF_DAY, MAX_HOUR_OF_DAY)
            set(Calendar.MINUTE, MAX_MINUTE_OF_DAY)
        }

        var defaultDate = GregorianCalendar(context?.let { LocaleUtils.getCurrentLocale(it) }).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        timePicker = context?.let { timePickerContext ->
            DateTimePickerUnify(
                timePickerContext,
                minTime,
                defaultDate,
                maxTime,
                listener,
                DateTimePickerUnify.TYPE_TIMEPICKER
            )
        }

        timePicker?.apply {
            hourInterval = 1
            minuteInterval = 1

            setTitle(
                this@VoucherEditCalendarBottomSheet.getString(R.string.edit_period_time_picker_title)
                    .toBlankOrString()
            )
            setInfo(
                "${
                this@VoucherEditCalendarBottomSheet.getString(R.string.edit_period_calender_title).toBlankOrString()}: $selectedDate"
            )
            setInfoVisible(true)
            datePickerButton.setOnClickListener {
            }
        }

        timePicker?.show(childFragmentManager, "TimePicker Show")
    }

    private fun renderCalendar(holidayArrayList: ArrayList<Legend>) {
        var selectedDates = startCalendar?.let {
            arrayListOf(it.time)
        }

        startDate?.time?.let {
            endDate?.time?.let { it1 ->
                calendar?.init(it, it1, holidayArrayList)
                    ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                    ?.withSelectedDates(selectedDates)
            }
        }

        calendar?.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
            override fun onNotifyMax() {
                Toast.makeText(activity, "woy udh max", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(
            startCalendar: GregorianCalendar?,
            minDate: GregorianCalendar,
            maxDate: GregorianCalendar,
            hour: Int,
            minute: Int
        ): VoucherEditCalendarBottomSheet {
            return VoucherEditCalendarBottomSheet().apply {
                this.startDate = minDate
                this.endDate = maxDate
                this.startCalendar = startCalendar
                this.hour = hour
                this.minute = minute
            }
        }

        const val MAX_HOUR_OF_DAY = 23
        const val MAX_MINUTE_OF_DAY = 59
        const val MIN_TIME_OF_DAY = 0
    }
}
