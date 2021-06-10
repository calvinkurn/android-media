package com.tokopedia.flight.search_universal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 10/03/2020
 */
class FlightSearchUniversalViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

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

    fun validateDepartureDate(departureDate: Date): Int {
        var resultStringResourceId = -1

        val oneYears = FlightDateUtil.removeTime(FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE, -1))

        if (departureDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_departure_max_one_years_from_today_error
        } else if (departureDate.before(FlightDateUtil.removeTime(FlightDateUtil.getCurrentDate()))) {
            resultStringResourceId = R.string.flight_dashboard_departure_should_atleast_today_error
        }

        return resultStringResourceId
    }

    fun validateReturnDate(departureDate: Date, returnDate: Date): Int {
        var resultStringResourceId = -1

        val oneYears = FlightDateUtil.removeTime(FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE, -1))

        if (returnDate.after(oneYears)) {
            resultStringResourceId = R.string.flight_dashboard_return_max_one_years_from_today_error
        } else if (returnDate.before(FlightDateUtil.removeTime(departureDate))) {
            resultStringResourceId = R.string.flight_dashboard_return_should_greater_equal_error
        }

        return resultStringResourceId
    }

    companion object {
        const val MAX_YEAR_FOR_FLIGHT = 1
        const val MINUS_ONE_DAY = -1
        const val DEFAULT_LAST_HOUR_IN_DAY = 23
        const val DEFAULT_LAST_MIN = 59
        const val DEFAULT_LAST_SEC = 59
    }

}