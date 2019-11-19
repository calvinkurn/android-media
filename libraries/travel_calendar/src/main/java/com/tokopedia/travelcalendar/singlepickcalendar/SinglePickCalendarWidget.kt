package com.tokopedia.travelcalendar.singlecalendar

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.travelcalendar.*
import com.tokopedia.travelcalendar.viewmodel.TravelHolidayCalendarViewModel
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SinglePickCalendarWidget : RoundedBottomSheetDialogFragment() {

    private lateinit var calendarUnify: UnifyCalendar
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var listenerCalendar: ActionListener

    private lateinit var btnClose: ImageButton
    private lateinit var holidayCalendarViewModel: TravelHolidayCalendarViewModel

    lateinit var minDate: Date
    lateinit var maxDate: Date
    lateinit var selectedDate: Date

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    fun initInjector() {
        val component = TravelCalendarComponentInstance
                .getComponent(activity?.application as Application)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_calendar_single_pick, container, false)
        btnClose = view.findViewById(R.id.btn_close)
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar)
        calendarUnify = view.findViewById(R.id.calendar_unify)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar.visibility = View.VISIBLE
        holidayCalendarViewModel.getCalendarHoliday()
        holidayCalendarViewModel.holidayCalendarData.observe(this, androidx.lifecycle.Observer {
            loadingProgressBar.visibility = View.GONE
            it?.let {
                renderSinglePickCalendar(it)
            }
        })

        btnClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    open fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        val calendar = calendarUnify.calendarPickerView

        calendar?.init(minDate, maxDate, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (listenerCalendar != null) {
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

        private const val MIN_DATE = "min_date"
        private const val MAX_DATE = "max_date"
        private const val SELECTED_DATE = "selected_date"

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