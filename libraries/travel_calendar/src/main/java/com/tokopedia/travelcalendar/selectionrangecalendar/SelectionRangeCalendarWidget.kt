package com.tokopedia.travelcalendar.selectionrangecalendar

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY_MM_DD
import com.tokopedia.travelcalendar.TravelCalendarComponentInstance
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.stringToDate
import com.tokopedia.unifycomponents.BottomSheetUnify
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

open class SelectionRangeCalendarWidget : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var selectionRangeCalendarViewModel: SelectionRangeCalendarViewModel
    lateinit var calendar: CalendarPickerView

    var isFirstTime: Boolean = true

    var listener: OnDateClickListener? = null
    var listenerMaxRange: OnNotifyMaxRange? = null

    var minDate: Date? = null
    var maxDate: Date? = null
    var rangeYear: Int = 0
    var rangeDateSelected: Long = 0
    var minDateLabel: String = ""
    var maxDateLabel: String = ""
    var minSelectableDateFromToday: Int = 0
    var canSelectSameDay: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(CALENDAR_TITLE)
        isFullpage = true
        showCloseIcon = true
        setCloseClickListener { this.dismissAllowingStateLoss() }

        val childView = View.inflate(context, R.layout.dialog_calendar_multi_pick, null)
        setChild(childView)

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            selectionRangeCalendarViewModel = viewModelProvider.get(SelectionRangeCalendarViewModel::class.java)
        }

        arguments?.let {
            if (it.getString(ARG_MIN_DATE) != null)
                minDate = (it.getString(ARG_MIN_DATE)?:"").stringToDate(TRAVEL_CAL_YYYY_MM_DD)

            if (it.getString(ARG_MAX_DATE) != null)
                maxDate = (it.getString(ARG_MAX_DATE)?:"").stringToDate(TRAVEL_CAL_YYYY_MM_DD)

            rangeYear = it.getInt(ARG_RANGE_YEAR)

            rangeDateSelected = it.getLong(ARG_RANGE_DATE_SELECTED)

            if (it.getString(ARG_MIN_DATE_LABEL) != null)
                minDateLabel = it.getString(ARG_MIN_DATE_LABEL) ?: ""

            if (it.getString(ARG_MAX_DATE_LABEL) != null)
                maxDateLabel = it.getString(ARG_MAX_DATE_LABEL) ?: ""

            minSelectableDateFromToday = it.getInt(ARG_MIN_SELECTABLE_DATE_FROM_TODAY)

            canSelectSameDay = it.getBoolean(ARG_CAN_SELECT_SAME_DAY)
        }
    }

    private fun initInjector() {
        val component = TravelCalendarComponentInstance
                .getComponent(activity?.application as Application)
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text_checkin.text = minDateLabel
        text_checkout.text = maxDateLabel

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

        date_in.keyListener = null
        date_out.keyListener = null

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLayoutMargin()
    }

    fun setLayoutMargin() {
        var padding = resources.getDimension(R.dimen.layout_lvl2).toInt()
        bottomSheetWrapper.setPadding(0, padding, 0, 0)
        bottomSheetHeader.setPadding(padding, 0, padding, 0)
    }


    open fun renderCalendar(legends: ArrayList<Legend>) {
        calendar = calendar_unify.calendarPickerView as CalendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, rangeYear)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, minSelectableDateFromToday)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        minDate?.let { if (it.before(yesterday.time) || it.after(nextYear.time)) minDate = yesterday.time }
        maxDate?.let { if (minDate != null && (it.before(yesterday.time) || it.after(nextYear.time))) maxDate = yesterday.time }

        minDate?.let { minDate ->
            maxDate?.let { maxDate ->
                calendar.init(yesterday.time, nextYear.time, legends)
                        .inMode(CalendarPickerView.SelectionMode.RANGE)
                        .maxRange(rangeDateSelected)
                        .withSelectedDates(listOf(minDate, maxDate))
                date_in.requestFocus()
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
                if (date_in?.isFocused == true) date_out.requestFocus()
            } else {
                if (date_out?.isFocused == true) date_in.requestFocus()
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
                        minDate?.let { dateIn -> onDateInClicked(dateIn) }
                    } else if (minDate != null && maxDate == null && ((!canSelectSameDay && date.after(minDate)) || (canSelectSameDay && !date.before(minDate)))) {
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

    open fun onDateInClicked(dateIn: Date) { }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(holiday.attribute.date.stringToDate(TRAVEL_CAL_YYYY_MM_DD),
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
        const val ARG_MIN_DATE_LABEL = "arg_min_date_label"
        const val ARG_MAX_DATE_LABEL = "arg_max_date_label"
        const val ARG_RANGE_YEAR = "arg_range_year"
        const val ARG_RANGE_DATE_SELECTED = "arg_range_date_selected"
        const val ARG_MIN_SELECTABLE_DATE_FROM_TODAY = "arg_min_selectable_date_from_today"
        const val ARG_CAN_SELECT_SAME_DAY = "arg_can_select_same_day"

        const val DEFAULT_MIN_SELECTED_DATE_TODAY = 0
        const val DEFAULT_MIN_SELECTED_DATE_PLUS_1_DAY = 1
        const val DEFAULT_RANGE_DATE_SELECTED = 360
        const val DEFAULT_RANGE_DATE_SELECTED_ONE_MONTH = 30
        const val DEFAULT_RANGE_CALENDAR_YEAR = 1

        const val CALENDAR_TITLE = "Pilih Tanggal"

        fun getInstance(minDate: String?, maxDate: String?, rangeYear: Int,
                        rangeDateSelected: Long, minDateLabel: String,
                        maxDateLabel: String, minSelectableDateFromToday: Int = 0,
                        canSelectSameDay: Boolean = false): SelectionRangeCalendarWidget =
                SelectionRangeCalendarWidget().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_MIN_DATE, minDate)
                        putString(ARG_MAX_DATE, maxDate)
                        putInt(ARG_RANGE_YEAR, rangeYear)
                        putLong(ARG_RANGE_DATE_SELECTED, rangeDateSelected)
                        putString(ARG_MIN_DATE_LABEL, minDateLabel)
                        putString(ARG_MAX_DATE_LABEL, maxDateLabel)
                        putInt(ARG_MIN_SELECTABLE_DATE_FROM_TODAY, minSelectableDateFromToday)
                        putBoolean(ARG_CAN_SELECT_SAME_DAY, canSelectSameDay)
                    }
                }
    }
}