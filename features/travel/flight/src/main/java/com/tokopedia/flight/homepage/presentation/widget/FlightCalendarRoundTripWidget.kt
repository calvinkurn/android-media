package com.tokopedia.flight.homepage.presentation.widget

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.calendar.SubTitle
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.homepage.di.DaggerFlightHomepageComponent
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.presentation.viewmodel.FlightFareCalendarViewModel
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY_MM_DD
import com.tokopedia.travelcalendar.dateToString
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.stringToDate
import java.util.*

/**
 * @author by jessica on 29/09/20
 */

class FlightCalendarRoundTripWidget : SelectionRangeCalendarWidget() {

    private lateinit var fareCalendarViewModel: FlightFareCalendarViewModel

    lateinit var departureCode: String
    lateinit var arrivalCode: String
    var classFlight: Int = 0

    var minCalendarDate: Date = Date()
    var maxCalendarDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        arguments?.run {
            this.getString(ARG_DEPARTURE_CODE)?.let { departureCode = it }
            this.getString(ARG_ARRIVAL_CODE)?.let { arrivalCode = it }
            this.getInt(ARG_CLASS)?.let { classFlight = it }
            this.getString(ARG_MAX_SELECTABLE_DATE)?.let { maxCalendarDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD) }
            this.getString(ARG_MIN_DATE)?.let { minCalendarDate = it.stringToDate(TRAVEL_CAL_YYYY_MM_DD) }
        }

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        fareCalendarViewModel = viewModelProvider.get(FlightFareCalendarViewModel::class.java)
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
            mapFareParam[PARAM_YEAR] = minCalendarDate.dateToString(TRAVEL_CAL_YYYY)
            mapFareParam[PARAM_CLASS] = classFlight.toString()

            activity?.run {
                fareCalendarViewModel.getFareFlightCalendar(mapFareParam, minCalendarDate, maxCalendarDate,
                        true, TravelDateUtil.dateToString(TRAVEL_CAL_YYYY_MM_DD, minDate))
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
            val dateFare = TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, it.dateFare)
            minDate?.let { date ->
                subTitleList.add(SubTitle(dateFare,
                        if (!dateFare.before(date)) it.displayedFare else " ", if (it.isLowestFare) getString(R.string.flight_calendar_lowest_fare_price_color) else ""))
            }
        }
        return subTitleList
    }

    override fun onDateInClicked(dateIn: Date) {
        calendar.showSubTitle(false)
        fareCalendarViewModel.calculateRoundTripFareCalendar(TravelDateUtil.dateToString(TRAVEL_CAL_YYYY_MM_DD, dateIn))
    }

    companion object {
        private const val ARG_DEPARTURE_CODE = "arg_departure_code"
        private const val ARG_ARRIVAL_CODE = "arg_arrival_code"
        private const val ARG_CLASS = "arg_class"
        private const val ARG_MAX_SELECTABLE_DATE = "arg_max_selectable_date"

        const val PARAM_DEPARTURE_CODE = "departCode"
        const val PARAM_ARRIVAL_CODE = "arrivalCode"
        const val PARAM_YEAR = "year"
        private const val PARAM_MONTH = "month"
        private const val PARAM_CLASS = "class"

        fun getInstance(minDate: String?, maxDate: String?, rangeYear: Int,
                        rangeDateSelected: Long, minDateLabel: String,
                        maxDateLabel: String, minSelectableDateFromToday: Int = 0,
                        canSelectSameDay: Boolean = false,
                        departureCode: String, arrivalCode: String, classFlight: Int,
                        maxSelectableDate: String): FlightCalendarRoundTripWidget =
                FlightCalendarRoundTripWidget().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_MIN_DATE, minDate)
                        putString(ARG_MAX_DATE, maxDate)
                        putInt(ARG_RANGE_YEAR, rangeYear)
                        putLong(ARG_RANGE_DATE_SELECTED, rangeDateSelected)
                        putString(ARG_MIN_DATE_LABEL, minDateLabel)
                        putString(ARG_MAX_DATE_LABEL, maxDateLabel)
                        putInt(ARG_MIN_SELECTABLE_DATE_FROM_TODAY, minSelectableDateFromToday)
                        putBoolean(ARG_CAN_SELECT_SAME_DAY, canSelectSameDay)
                        putString(ARG_DEPARTURE_CODE, departureCode)
                        putString(ARG_ARRIVAL_CODE, arrivalCode)
                        putInt(ARG_CLASS, classFlight)
                        putString(ARG_MAX_SELECTABLE_DATE, maxSelectableDate)
                    }
                }
    }
}