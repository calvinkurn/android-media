package com.tokopedia.travelcalendar.selectionrangecalendar

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY_MM_DD
import com.tokopedia.travelcalendar.TravelCalendarComponentInstance
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.databinding.DialogCalendarMultiPickBinding
import com.tokopedia.travelcalendar.stringToDate
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 03/07/19
 */

open class SelectionRangeCalendarWidget : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected var binding by autoClearedNullable<DialogCalendarMultiPickBinding>()

    lateinit var selectionRangeCalendarViewModel: SelectionRangeCalendarViewModel

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

        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            selectionRangeCalendarViewModel =
                viewModelProvider.get(SelectionRangeCalendarViewModel::class.java)
        }

        arguments?.let {
            minDate = it.getString(ARG_MIN_DATE)?.stringToDate(TRAVEL_CAL_YYYY_MM_DD)

            maxDate = it.getString(ARG_MAX_DATE)?.stringToDate(TRAVEL_CAL_YYYY_MM_DD)

            rangeYear = it.getInt(ARG_RANGE_YEAR)

            rangeDateSelected = it.getLong(ARG_RANGE_DATE_SELECTED)

            minDateLabel = it.getString(ARG_MIN_DATE_LABEL).orEmpty()

            maxDateLabel = it.getString(ARG_MAX_DATE_LABEL).orEmpty()

            minSelectableDateFromToday = it.getInt(ARG_MIN_SELECTABLE_DATE_FROM_TODAY)

            canSelectSameDay = it.getBoolean(ARG_CAN_SELECT_SAME_DAY)
        }
    }

    private fun initInjector() {
        val component = TravelCalendarComponentInstance
            .getComponent(activity?.application as Application)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        binding = DialogCalendarMultiPickBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.run {
            textCheckin.text = minDateLabel
            textCheckout.text = maxDateLabel

            loadingProgressBar.visibility = View.VISIBLE
            selectionRangeCalendarViewModel.getTravelHolidayDate()

            selectionRangeCalendarViewModel.holidayResult.observe(
                this@SelectionRangeCalendarWidget,
                Observer {
                    loadingProgressBar.visibility = View.GONE
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
                }
            )

            context?.let {
                dateIn.typeface = Typography.getFontType(it, true, Typography.DISPLAY_1)
                dateOut.typeface = Typography.getFontType(it, true, Typography.DISPLAY_1)
            }

            dateIn.keyListener = null
            dateOut.keyListener = null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLayoutMargin()
    }

    fun setLayoutMargin() {
        context?.let {
            var padding = it.resources.getDimension(unifyprinciplesR.dimen.layout_lvl2).toInt()
            bottomSheetWrapper.setPadding(0, padding, 0, 0)
            bottomSheetHeader.setPadding(padding, 0, padding, 0)
        }
    }

    open fun renderCalendar(legends: ArrayList<Legend>) {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, rangeYear)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, minSelectableDateFromToday)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        minDate?.let {
            if (it.before(yesterday.time) || it.after(nextYear.time)) minDate = yesterday.time
        }
        maxDate?.let {
            if (minDate != null && (it.before(yesterday.time) || it.after(nextYear.time))) {
                maxDate =
                    yesterday.time
            }
        }

        binding?.run {
            minDate?.let { minDate ->
                maxDate?.let { maxDate ->
                    calendarUnify.calendarPickerView?.init(yesterday.time, nextYear.time, legends)
                        ?.inMode(CalendarPickerView.SelectionMode.RANGE)
                        ?.maxRange(rangeDateSelected)
                        ?.withSelectedDates(listOf(minDate, maxDate))
                    dateIn.requestFocus()
                }
            }

            if (maxDate == null) {
                minDate?.let {
                    calendarUnify.calendarPickerView?.let { calendar ->
                        calendar.init(yesterday.time, nextYear.time, legends)
                            .inMode(CalendarPickerView.SelectionMode.RANGE)
                            .maxRange(rangeDateSelected)
                            .withSelectedDate(it)
                        dateOut.requestFocus()
                    }
                }
            }

            val defaultIndLocale = Locale("id", "ID")
            val dateFormat = SimpleDateFormat("E, d MMM", defaultIndLocale)
            dateFormat.timeZone = TimeZone.getDefault()

            if (minDate != null) dateIn.setText(dateFormat.format(minDate))
            if (maxDate != null) dateOut.setText(dateFormat.format(maxDate))

            dateIn.setOnFocusChangeListener { view, hasFocus ->
                if (maxDate == null) {
                    if (dateIn.isFocused == true) dateOut.requestFocus()
                } else {
                    if (dateOut.isFocused == true) dateIn.requestFocus()
                }
            }

            calendarUnify.calendarPickerView?.let {
                it.setMaxRangeListener(object : CalendarPickerView.OnMaxRangeListener {
                    override fun onNotifyMax() {
                        listenerMaxRange?.onNotifyMax()
                    }
                })

                it.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {

                    override fun onDateSelected(date: Date) {
                        if ((minDate != null && maxDate != null) || (
                            maxDate == null && date.before(
                                    minDate
                                )
                            )
                        ) {
                            dateIn.setText(dateFormat.format(date))
                            dateOut.setText("")
                            minDate = date
                            maxDate = null
                            dateOut.requestFocus()
                            minDate?.let { dateIn -> onDateInClicked(dateIn) }
                        } else if (minDate != null && maxDate == null && (
                            (
                                !canSelectSameDay && date.after(
                                        minDate
                                    )
                                ) || (canSelectSameDay && !date.before(minDate))
                            )
                        ) {
                            if ((it.selectedDates.size > 1 && !canSelectSameDay) || canSelectSameDay) {
                                dateOut.setText(dateFormat.format(date))
                                maxDate = date
                                dateOut.requestFocus()
                                if (listener != null) {
                                    listener?.onDateClick(
                                        minDate ?: Date(),
                                        maxDate
                                            ?: date
                                    )
                                    GlobalScope.launch {
                                        delay(DISMISS_DELAY)
                                        dismissAllowingStateLoss()
                                    }
                                }
                            } else {
                                minDate = date
                                maxDate = null
                            }
                        }
                    }

                    override fun onDateUnselected(date: Date) {
                    }
                })
            }
        }
    }

    open fun onDateInClicked(dateIn: Date) {}

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(
                Legend(
                    holiday.attribute.date.stringToDate(TRAVEL_CAL_YYYY_MM_DD),
                    holiday.attribute.label
                )
            )
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

        private const val DISMISS_DELAY: Long = 300

        fun getInstance(
            minDate: String?,
            maxDate: String?,
            rangeYear: Int,
            rangeDateSelected: Long,
            minDateLabel: String,
            maxDateLabel: String,
            minSelectableDateFromToday: Int = 0,
            canSelectSameDay: Boolean = false
        ): SelectionRangeCalendarWidget =
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
