package com.tokopedia.flight.dashboard.view.widget

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.SubTitle
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent
import com.tokopedia.flight.dashboard.view.model.FlightFareAttributes
import com.tokopedia.flight.dashboard.view.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.flight.dashboard.view.viewmodel.FlightHolidayCalendarViewModel
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY
import com.tokopedia.travelcalendar.dateToString
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FlightCalendarOneWayWidget : RoundedBottomSheetDialogFragment() {

    private lateinit var btnClose: ImageButton
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var calendarUnify: UnifyCalendar
    private lateinit var listener: ActionListener
    private lateinit var holidayCalendarViewModel: FlightHolidayCalendarViewModel
    private lateinit var fareCalendarViewModel: FlightFareCalendarViewModel
    private lateinit var titlePage: TextView

    lateinit var minDate: Date
    lateinit var maxDate: Date
    lateinit var selectedDate: Date
    lateinit var departureCode: String
    lateinit var arrivalCode: String

    var isFirstTime: Boolean = true

    var classFlight: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            holidayCalendarViewModel = viewModelProvider.get(FlightHolidayCalendarViewModel::class.java)
            fareCalendarViewModel = viewModelProvider.get(FlightFareCalendarViewModel::class.java)
        }

        arguments?.run {
            this.getString(ARG_MIN_DATE)?.let {
                minDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }

            this.getString(ARG_MAX_DATE)?.let {
                maxDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }

            this.getString(ARG_SELECTED_DATE)?.let {
                selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }

            this.getString(ARG_DEPARTURE_CODE)?.let {
                departureCode = it
            }

            this.getString(ARG_ARRIVAL_CODE)?.let {
                arrivalCode = it
            }

            this.getInt(ARG_CLASS)?.let {
                classFlight = it
            }
        }
    }

    fun initInjector() {
        val component = DaggerFlightDashboardComponent.builder()
                .flightComponent(FlightComponentInstance
                        .getFlightComponent(activity?.application as Application))
                .build()
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.travelcalendar.R.layout.dialog_calendar_single_pick, container, false)
        btnClose = view.findViewById(com.tokopedia.travelcalendar.R.id.btn_close)
        loadingProgressBar = view.findViewById(com.tokopedia.travelcalendar.R.id.loading_progress_bar)
        calendarUnify = view.findViewById(com.tokopedia.travelcalendar.R.id.calendar_unify)
        titlePage = view.findViewById(com.tokopedia.travelcalendar.R.id.tv_title)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titlePage.text = "Pilih Tanggal"

        loadingProgressBar.visibility = View.VISIBLE
        holidayCalendarViewModel.getCalendarHoliday()
        holidayCalendarViewModel.holidayCalendarData.observe(this, androidx.lifecycle.Observer {
            loadingProgressBar.visibility = View.GONE
            it?.let {
                if (isFirstTime) {
                    renderSinglePickCalendar(it)
                    isFirstTime = false
                }
            }
        })



        btnClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    private fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        val calendar = calendarUnify.calendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        calendar?.init(minDate, nextYear.time, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        if (::departureCode.isInitialized && ::arrivalCode.isInitialized && classFlight > 0) {

            val mapFareParam = hashMapOf<String, Any>()
            mapFareParam[PARAM_DEPARTURE_CODE] = departureCode
            mapFareParam[PARAM_ARRIVAL_CODE] = arrivalCode
            mapFareParam[PARAM_YEAR] = minDate.dateToString(TRAVEL_CAL_YYYY)
            mapFareParam[PARAM_CLASS] = classFlight.toString()
            activity?.run {
                fareCalendarViewModel.getFareFlightCalendar(
                        GraphqlHelper.loadRawString(this.resources, R.raw.flight_fare_calendar_query),
                        mapFareParam, minDate, maxDate)
            }


            fareCalendarViewModel.fareFlightCalendarData.observe(this, androidx.lifecycle.Observer {
                it?.let {
                    calendar?.setSubTitles(mapFareFlightToSubtitleCalendar(it))
                }
            })

        }

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                if (::listener.isInitialized) {
                    listener.onDateSelected(date)

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

    private fun mapFareFlightToSubtitleCalendar(listFareAttribute: List<FlightFareAttributes>): ArrayList<SubTitle> {
        val subTitleList = arrayListOf<SubTitle>()
        listFareAttribute.map {
            subTitleList.add(SubTitle(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.dateFare),
                    it.displayedFare, if (it.isLowestFare) getString(R.string.flight_calendar_lowest_fare_price_color) else ""))
        }
        return subTitleList
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun onDateSelected(dateSelected: Date)
    }

    companion object {

        private const val ARG_MIN_DATE = "arg_min_date"
        private const val ARG_MAX_DATE = "arg_max_date"
        private const val ARG_SELECTED_DATE = "arg_selected_date"
        private const val ARG_DEPARTURE_CODE = "arg_departure_code"
        private const val ARG_ARRIVAL_CODE = "arg_arrival_code"
        private const val ARG_CLASS = "arg_class"

        private const val PARAM_DEPARTURE_CODE = "departCode"
        private const val PARAM_ARRIVAL_CODE = "arrivalCode"
        const val PARAM_YEAR = "year"
        private const val PARAM_MONTH = "month"
        private const val PARAM_CLASS = "class"

        fun newInstance(minDateString: String, maxDateString: String,
                        selectedDate: String, departureCode: String, arrivalCode: String,
                        classFlight: Int): FlightCalendarOneWayWidget =
                FlightCalendarOneWayWidget().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_MIN_DATE, minDateString)
                        putString(ARG_MAX_DATE, maxDateString)
                        putString(ARG_SELECTED_DATE, selectedDate)
                        putString(ARG_DEPARTURE_CODE, departureCode)
                        putString(ARG_ARRIVAL_CODE, arrivalCode)
                        putInt(ARG_CLASS, classFlight)
                    }
                }
    }

}