package com.tokopedia.flight.search_universal.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * @author by furqan on 23/06/2020
 */
class FlightSearchUniversalViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: FlightSearchUniversalViewModel

    @Before
    fun setUp() {
        viewModel = FlightSearchUniversalViewModel(CoroutineTestDispatchersProvider)
    }

    @Test
    fun generatePairOfMinMaxDateForDeparture_shouldReturnPairOfMinMaxForDeparture() {
        // given
        val minDate = FlightDateUtil.getCurrentDate()
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                FlightSearchUniversalViewModel.MINUS_ONE_DAY)

        // when
        val pairOfDeparture = viewModel.generatePairOfMinAndMaxDateForDeparture()

        // then
        pairOfDeparture.first.day shouldBe minDate.day
        pairOfDeparture.second.day shouldBe maxDate.day
        pairOfDeparture.second.hours shouldBe 23
        pairOfDeparture.second.minutes shouldBe 59
        pairOfDeparture.second.seconds shouldBe 59
    }

    @Test
    fun generatePairOfMinMaxDateForReturn_shouldReturnPairOfMinMaxForReturn() {
        // given
        val departureDate = FlightDateUtil.getCurrentDate()
        val maxDate = FlightDateUtil.addTimeToSpesificDate(
                FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, FlightSearchUniversalViewModel.MAX_YEAR_FOR_FLIGHT),
                Calendar.DATE,
                FlightSearchUniversalViewModel.MINUS_ONE_DAY)

        // when
        val pairOfDeparture = viewModel.generatePairOfMinAndMaxDateForReturn(departureDate)

        // then
        pairOfDeparture.first.day shouldBe departureDate.day
        pairOfDeparture.second.day shouldBe maxDate.day
        pairOfDeparture.second.hours shouldBe 23
        pairOfDeparture.second.minutes shouldBe 59
        pairOfDeparture.second.seconds shouldBe 59
    }

    @Test
    fun validateDepartureDate_withValidDepartureDate_shouldSuccess() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)

        // when
        val validation = viewModel.validateDepartureDate(departureDate)

        // then
        validation shouldBe -1
    }

    @Test
    fun validateDepartureDate_withDepartureDateMoreThanYear_shouldFailed() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 10)

        // when
        val validation = viewModel.validateDepartureDate(departureDate)

        // then
        validation shouldBe R.string.flight_dashboard_departure_max_one_years_from_today_error
    }

    @Test
    fun validateDepartureDate_withDepartureDateBeforeToday_shouldFailed() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, -1)

        // when
        val validation = viewModel.validateDepartureDate(departureDate)

        // then
        validation shouldBe R.string.flight_dashboard_departure_should_atleast_today_error
    }

    @Test
    fun validateReturnDate_withValidReturnDate_shouldSuccess() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 2)

        // when
        val validation = viewModel.validateReturnDate(departureDate, returnDate)

        // then
        validation shouldBe -1
    }

    @Test
    fun validateReturnDate_withReturnDateMoreThanYear_shouldFailed() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 10)

        // when
        val validation = viewModel.validateReturnDate(departureDate, returnDate)

        // then
        validation shouldBe R.string.flight_dashboard_return_max_one_years_from_today_error
    }

    @Test
    fun validateReturnDate_withReturnDateBeforeDeparture_shouldFailed() {
        // given
        val departureDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 2)
        val returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.MONTH, 1)

        // when
        val validation = viewModel.validateReturnDate(departureDate, returnDate)

        // then
        validation shouldBe R.string.flight_dashboard_return_should_greater_equal_error
    }
}