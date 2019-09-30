package com.tokopedia.flight.dashboard.view.widget

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.calendar.SubTitle
import com.tokopedia.calendar.UnifyCalendar
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent
import com.tokopedia.flight.dashboard.view.model.FlightFareAttributes
import com.tokopedia.flight.dashboard.view.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.flight.dashboard.view.viewmodel.FlightHolidayCalendarViewModel
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FlightCalendarOneWayWidget : RoundedBottomSheetDialogFragment(), HasComponent<FlightDashboardComponent> {

    private lateinit var btnClose: ImageButton
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var calendarUnify: UnifyCalendar
    private lateinit var listener: ActionListener
    private lateinit var holidayCalendarViewModel: FlightHolidayCalendarViewModel
    private lateinit var fareCalendarViewModel: FlightFareCalendarViewModel

    lateinit var minDate: Date
    lateinit var maxDate: Date
    lateinit var selectedDate: Date

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
            this.getString(MIN_DATE)?.let {
                minDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }

            this.getString(MAX_DATE)?.let {
                maxDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }

            this.getString(SELECTED_DATE)?.let {
                selectedDate = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it)
            }
        }
    }

    override fun getComponent(): FlightDashboardComponent =
            DaggerFlightDashboardComponent.builder()
                    .flightComponent(FlightComponentInstance
                            .getFlightComponent(activity?.application as Application))
                    .build()

    fun initInjector() {
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_calendar_one_way, container, false)
        btnClose = view.findViewById(R.id.btn_close)
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar)
        calendarUnify = view.findViewById(R.id.calendar_unify)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar.visibility = View.VISIBLE
        holidayCalendarViewModel.getCalendarHoliday()
        holidayCalendarViewModel.holidayCalendarData.observe(this, android.arch.lifecycle.Observer {
            loadingProgressBar.visibility = View.GONE
            it?.let {
                renderSinglePickCalendar(it)
            }
        })

        btnClose.setOnClickListener { dismissAllowingStateLoss() }
    }

    private fun renderSinglePickCalendar(holidayArrayList: ArrayList<Legend>) {
        val calendar = calendarUnify.calendarPickerView

        calendar?.init(minDate, maxDate, holidayArrayList)
                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                ?.withSelectedDate(selectedDate)

        fareCalendarViewModel.fareFlightCalendarData.observe(this, android.arch.lifecycle.Observer {
            it?.let {
                calendar?.setSubTitles(mapFareFlightToSubtitleCalendar(it))
            }
        })

        calendar?.run {
            this.onScrollMonthListener = object : CalendarPickerView.OnScrollMonthListener {
                override fun onScrolled(date: Date) {
                    val mapFareParam = hashMapOf<String, Any>()
                    mapFareParam.put("departCode", "CGK")
                    mapFareParam.put("arrivalCode", "DPS")
                    mapFareParam.put("year", TravelDateUtil.dateToString(TravelDateUtil.YYYY, date))
                    mapFareParam.put("month", TravelDateUtil.dateToString("M", date))
                    mapFareParam.put("class", "1")
                    activity?.run {
                        fareCalendarViewModel.getFareFlightCalendar(
                                GraphqlHelper.loadRawString(this.resources, R.raw.flight_fare_calendar_query),
                                mapFareParam)
                    }
                }
            }
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
                    (it.cheapestPriceNumeric / 1000).toString()))
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

        private const val MIN_DATE = "min_date"
        private const val MAX_DATE = "max_date"
        private const val SELECTED_DATE = "selected_date"

        fun newInstance(minDateString: String, maxDateString: String,
                        selectedDate: String): FlightCalendarOneWayWidget =
                FlightCalendarOneWayWidget().also {
                    it.arguments = Bundle().apply {
                        putString(MIN_DATE, minDateString)
                        putString(MAX_DATE, maxDateString)
                        putString(SELECTED_DATE, selectedDate)
                    }
                }
    }

}