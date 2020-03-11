package com.tokopedia.flight.search_universal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardPassDataViewModel
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 10/03/2020
 */
class FlightSearchUniversalViewModel @Inject constructor(
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    val flightSearchPassData = FlightDashboardPassDataViewModel()

    fun generatePairOfMinAndMaxDateForDeparture(): Pair<Date, Date> {
        val minDate = FlightDateUtil.getCurrentDate()
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                MINUS_ONE_DAY)
        val maxDateCalendar = FlightDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_SEC)
        return Pair(minDate, maxDateCalendar.time)
    }

    fun generatePairOfMinAndMaxDateForReturn(departureDate: Date): Pair<Date, Date> {
        val minDate = departureDate
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                MINUS_ONE_DAY)
        val maxDateCalendar = FlightDateUtil.getCurrentCalendar()
        maxDateCalendar.time = maxDate
        maxDateCalendar.set(Calendar.HOUR_OF_DAY, DEFAULT_LAST_HOUR_IN_DAY)
        maxDateCalendar.set(Calendar.MINUTE, DEFAULT_LAST_MIN)
        maxDateCalendar.set(Calendar.SECOND, DEFAULT_LAST_SEC)
        return Pair(minDate, maxDateCalendar.time)
    }

    companion object {
        const val MAX_YEAR_FOR_FLIGHT = 1
        const val MINUS_ONE_DAY = -1
        const val DEFAULT_LAST_HOUR_IN_DAY = 23
        const val DEFAULT_LAST_MIN = 59
        const val DEFAULT_LAST_SEC = 59
    }

}