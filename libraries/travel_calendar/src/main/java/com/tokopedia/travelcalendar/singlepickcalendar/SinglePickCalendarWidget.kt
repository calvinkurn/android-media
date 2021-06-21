package com.tokopedia.travelcalendar.singlecalendar

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.*
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.viewmodel.TravelHolidayCalendarViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.dialog_calendar_single_pick.loading_progress_bar
import kotlinx.android.synthetic.main.dialog_calendar_single_pick.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

open class SinglePickCalendarWidget : BottomSheetUnify() {
    lateinit var calendar: CalendarPickerView

    private lateinit var listenerCalendar: ActionListener

    private lateinit var holidayCalendarViewModel: TravelHolidayCalendarViewModel

    var minDate: Date = Date()
    var maxDate: Date = Date()
    var selectedDate: Date = Date()

    var isFirstTime: Boolean = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(SelectionRangeCalendarWidget.CALENDAR_TITLE)
        isFullpage = true
        showCloseIcon = true
        setCloseClickListener { this.dismissAllowingStateLoss() }

        initInjector()

        initViewModelProvider()

        getBundleData()
    }

    fun initViewModelProvider() {
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            holidayCalendarViewModel = viewModelProvider.get(TravelHolidayCalendarViewModel::class.java)
        }
    }

    fun getBundleData() {
        arguments?.run {
            this.getString(MIN_DATE)?.let {
                minDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
            }

            this.getString(MAX_DATE)?.let {
                maxDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
            }

            this.getString(SELECTED_DATE)?.let {
                selectedDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD)
            }
        }
    }

    private fun initInjector() {
        val component = TravelCalendarComponentInstance
                .getComponent(activity?.application as Application)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun initChildLayout(){
        val childView = View.inflate(context, R.layout.dialog_calendar_single_pick, null)
        calendar = childView.calendar_unify.calendarPickerView as CalendarPickerView
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_progress_bar.visibility = View.VISIBLE
        holidayCalendarViewModel.getCalendarHoliday()
        holidayCalendarViewModel.holidayCalendarData.observe(this, androidx.lifecycle.Observer {
            loading_progress_bar.visibility = View.GONE
            it?.let {
                if (isFirstTime) {
                    renderSinglePickCalendar(it)
                    isFirstTime = false
                }
            }
        })

    }

    open fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR,1)

        calendar?.init(minDate, nextYear.time, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (::listenerCalendar.isInitialized) {
                    listenerCalendar?.onDateSelected(date)

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

    fun setListener(listener: ActionListener) {
        this.listenerCalendar = listener
    }

    interface ActionListener {
        fun onDateSelected(dateSelected: Date)
    }

    companion object {
        const val MIN_DATE = "min_date"
        const val MAX_DATE = "max_date"
        const val SELECTED_DATE = "selected_date"

        fun newInstance(minDateString: String, maxDateString: String,
                        selectedDate: String): SinglePickCalendarWidget =
                SinglePickCalendarWidget().also {
                    it.arguments = Bundle().apply {
                        putString(MIN_DATE, minDateString)
                        putString(MAX_DATE, maxDateString)
                        putString(SELECTED_DATE, selectedDate)
                    }
                }
    }

}