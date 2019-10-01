package com.tokopedia.travelcalendar.selectionrangecalendar

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.TravelCalendarComponentInstance
import com.tokopedia.travelcalendar.YYYY_MM_DD
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.stringToDate
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.dialog_calendar_multi_pick.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author by jessica on 03/07/19
 */

open class SelectionRangeCalendarWidget : RoundedBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var selectionRangeCalendarViewModel: SelectionRangeCalendarViewModel
    lateinit var calendar: CalendarPickerView

    var isFirstTime: Boolean = true

    var listener: OnDateClickListener? = null
    var listenerMaxRange: OnNotifyMaxRange? = null
    var title: String = "Pilih Tanggal"

    var minDate: Date? = null
    var maxDate: Date? = null
    var rangeYear: Int = 0
    var rangeDateSelected: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            selectionRangeCalendarViewModel = viewModelProvider.get(SelectionRangeCalendarViewModel::class.java)
        }

        arguments?.let {
            if (it.getString(ARG_MIN_DATE) != null)
                minDate = it.getString(ARG_MIN_DATE).stringToDate(YYYY_MM_DD)

            if (it.getString(ARG_MAX_DATE) != null)
                maxDate = it.getString(ARG_MAX_DATE).stringToDate(YYYY_MM_DD)

            if (it.getInt(ARG_RANGE_YEAR) != null)
                rangeYear = it.getInt(ARG_RANGE_YEAR)

            if (it.getLong(ARG_RANGE_DATE_SELECTED) != null)
                rangeDateSelected = it.getLong(ARG_RANGE_DATE_SELECTED)
        }
    }

    private fun initInjector() {
        val component = TravelCalendarComponentInstance
                .getComponent(activity?.application as Application)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_calendar_multi_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_progress_bar.visibility = View.VISIBLE

        selectionRangeCalendarViewModel.getTravelHolidayDate()

        selectionRangeCalendarViewModel.holidayResult.observe(this, Observer {
            loading_progress_bar.visibility = View.GONE
            when (it) {
                is Success -> {
                    if (isFirstTime && it.data.data.isNotEmpty()) {
                        renderCalendar(mappingHolidayData(it.data))
                        isFirstTime = false
                    }
                }
                is Fail -> {
                    renderCalendar(arrayListOf())
                }
            }
        })

        btn_close.setOnClickListener({ view1 -> dismissAllowingStateLoss() })

        date_in.keyListener = null
        date_out.keyListener = null

    }

    open fun renderCalendar(legends: ArrayList<Legend>) {
        calendar = calendar_unify.calendarPickerView as CalendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, rangeYear)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, 1)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        minDate?.let { minDate ->
            maxDate?.let { maxDate ->
                calendar.let { calendar ->
                    calendar.init(yesterday.time, nextYear.time, legends)
                            .inMode(CalendarPickerView.SelectionMode.RANGE)
                            .maxRange(rangeDateSelected)
                            .withSelectedDates(listOf(minDate, maxDate))
                    date_in.requestFocus()
                }
            }
        }

        if (maxDate == null) {
            minDate?.let {
                calendar.let { calendar ->
                    calendar.init(yesterday.time, nextYear.time, legends)
                            .inMode(CalendarPickerView.SelectionMode.RANGE)
                            .maxRange(rangeDateSelected)
                            .withSelectedDate(it)
                    date_out.requestFocus()
                }
            }
        }

        val defaultIndLocale = Locale("id", "ID")
        val dateFormat = SimpleDateFormat("E, d MMM", defaultIndLocale)
        dateFormat.timeZone = TimeZone.getDefault()

        if (minDate != null) date_in.setText(dateFormat.format(minDate))
        if (maxDate != null) date_out.setText(dateFormat.format(maxDate))

        date_in.setOnFocusChangeListener { view, hasFocus ->
            if (maxDate == null) {
                if (date_in.isFocused) date_out.requestFocus()
            } else {
                if (date_out.isFocused) date_in.requestFocus()
            }
        }

        calendar.let {

            it.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
                override fun onNotifyMax() {
                    listenerMaxRange?.onNotifyMax()
                }

            })

            it.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                override fun onDateSelected(date: Date) {

                    if ((minDate != null && maxDate != null) || (maxDate == null && date.before(minDate))) {
                        date_in.setText(dateFormat.format(date))
                        date_out.setText("")
                        minDate = date
                        maxDate = null
                        date_out.requestFocus()
                    } else if (minDate != null && maxDate == null && date.after(minDate)) {
                        date_out.setText(dateFormat.format(date))
                        maxDate = date
                        date_out.requestFocus()
                        if (listener != null) listener?.onDateClick(minDate ?: Date(), maxDate
                                ?: date)

                        GlobalScope.launch {
                            delay(300)
                            dismissAllowingStateLoss()
                        }
                    }
                }

                override fun onDateUnselected(date: Date) {

                }
            })
        }
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(holiday.attribute.date.stringToDate(YYYY_MM_DD),
                    holiday.attribute.label))
        }
        return legendList
    }

    interface OnDateClickListener {
        fun onDateClick(dateIn: Date, dateOut: Date)
    }

    interface OnNotifyMaxRange {
        fun onNotifyMax()
    }

    companion object {

        const val ARG_MIN_DATE = "arg_min_date"
        const val ARG_MAX_DATE = "arg_max_date"
        const val ARG_RANGE_YEAR = "arg_range_year"
        const val ARG_RANGE_DATE_SELECTED = "arg_range_date_selected"

        fun getInstance(minDate: String?, maxDate: String?, rangeYear: Int,
                        rangeDateSelected: Long): SelectionRangeCalendarWidget =
                SelectionRangeCalendarWidget().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_MIN_DATE, minDate)
                        putString(ARG_MAX_DATE, maxDate)
                        putInt(ARG_RANGE_YEAR, rangeYear)
                        putLong(ARG_RANGE_DATE_SELECTED, rangeDateSelected)
                    }
                }
    }
}