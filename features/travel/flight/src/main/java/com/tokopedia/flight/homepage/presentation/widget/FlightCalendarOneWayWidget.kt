package com.tokopedia.flight.homepage.presentation.widget

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.calendar.SubTitle
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.homepage.di.DaggerFlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY
import com.tokopedia.travelcalendar.dateToString
import com.tokopedia.travelcalendar.singlecalendar.SinglePickCalendarWidget
import java.util.*

class FlightCalendarOneWayWidget : SinglePickCalendarWidget() {

    private lateinit var fareCalendarViewModel: FlightFareCalendarViewModel

    lateinit var departureCode: String
    lateinit var arrivalCode: String

    var classFlight: Int = 0

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        fareCalendarViewModel = viewModelProvider.get(FlightFareCalendarViewModel::class.java)

        arguments?.run {
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
        val component = DaggerFlightHomepageComponent.builder()
                .flightComponent(FlightComponentInstance
                        .getFlightComponent(activity?.application as Application))
                .build()
        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::departureCode.isInitialized && ::arrivalCode.isInitialized && classFlight > 0) {

            val mapFareParam = hashMapOf<String, Any>()
            mapFareParam[PARAM_DEPARTURE_CODE] = departureCode
            mapFareParam[PARAM_ARRIVAL_CODE] = arrivalCode
            mapFareParam[PARAM_YEAR] = minDate.dateToString(TRAVEL_CAL_YYYY)
            mapFareParam[PARAM_CLASS] = classFlight.toString()

            activity?.run {
                fareCalendarViewModel.getFareFlightCalendar(mapFareParam, minDate, maxDate)
            }

            fareCalendarViewModel.fareFlightCalendarData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    calendar?.setSubTitles(mapFareFlightToSubtitleCalendar(it))
                }
            })
        }
    }

    private fun mapFareFlightToSubtitleCalendar(listFareAttribute: List<FlightFareAttributes>): ArrayList<SubTitle> {
        val subTitleList = arrayListOf<SubTitle>()
        listFareAttribute.map {
            subTitleList.add(SubTitle(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.dateFare),
                    it.displayedFare, if (it.isLowestFare) getString(R.string.flight_calendar_lowest_fare_price_color) else ""))
        }
        return subTitleList
    }

    companion object {
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
                        putString(MIN_DATE, minDateString)
                        putString(MAX_DATE, maxDateString)
                        putString(SELECTED_DATE, selectedDate)
                        putString(ARG_DEPARTURE_CODE, departureCode)
                        putString(ARG_ARRIVAL_CODE, arrivalCode)
                        putInt(ARG_CLASS, classFlight)
                    }
                }
    }

}