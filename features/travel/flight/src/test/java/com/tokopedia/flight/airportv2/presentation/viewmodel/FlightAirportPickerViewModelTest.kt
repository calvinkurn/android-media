package com.tokopedia.flight.airportv2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.flight.airportv2.domain.FlightAirportPopularCityUseCase
import com.tokopedia.flight.airportv2.domain.FlightAirportSuggestionUseCase
import com.tokopedia.flight.airportv2.presentation.model.FlightAirportModel
import com.tokopedia.flight.dummy.DUMMY_POPULAR_AIRPORT
import com.tokopedia.flight.dummy.DUMMY_SUGGESTION_AIRPORT
import com.tokopedia.flight.shouldBe
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 22/06/2020
 */
class FlightAirportPickerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private val flightPopularAirportUseCase: FlightAirportPopularCityUseCase = mockk()
    private val flightAirportSuggestionUseCase: FlightAirportSuggestionUseCase = mockk()

    private lateinit var flightAirportPickerViewModel: FlightAirportPickerViewModel

    @Before
    fun setUp() {
        flightAirportPickerViewModel = FlightAirportPickerViewModel(
                flightPopularAirportUseCase,
                flightAirportSuggestionUseCase,
                testDispatcherProvider
        )
    }

    @Test
    fun fetchAirportWithEmptyKeyword_fetchFailed_airportListShouldBeFail() {
        // given
        coEvery { flightPopularAirportUseCase.fetchAirportPopularCity() } coAnswers { throw Throwable("Fetch Failed") }

        // when
        flightAirportPickerViewModel.fetchAirport()

        // then
        assert(flightAirportPickerViewModel.airportList.value is Fail)
        (flightAirportPickerViewModel.airportList.value as Fail).throwable.message shouldBe "Fetch Failed"
    }

    @Test
    fun fetchAirportWithEmptyKeyword_fetchSuccessButEmpty_airportListShouldBeSuccessButEmpty() {
        // given
        coEvery { flightPopularAirportUseCase.fetchAirportPopularCity() } returns arrayListOf()

        // when
        flightAirportPickerViewModel.fetchAirport()

        // then
        assert(flightAirportPickerViewModel.airportList.value is Success)
        (flightAirportPickerViewModel.airportList.value as Success).data.size shouldBe 0
    }

    @Test
    fun fetchAirportWithEmptyKeyword_fetchSuccessAndContainsData_airportListShouldBeSuccessAndContainsData() {
        // given
        coEvery { flightPopularAirportUseCase.fetchAirportPopularCity() } returns DUMMY_POPULAR_AIRPORT

        // when
        flightAirportPickerViewModel.fetchAirport()

        // then
        assert(flightAirportPickerViewModel.airportList.value is Success)
        val airportList = (flightAirportPickerViewModel.airportList.value as Success).data
        airportList.size shouldBe DUMMY_POPULAR_AIRPORT.size

        for ((index, item) in airportList.withIndex()) {
            (item as FlightAirportModel).airportName shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).airportName
            item.airportCode shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).airportCode
            item.cityId shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).cityId
            item.cityCode shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).cityCode
            item.cityName shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).cityName
            item.countryName shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).countryName
            item.cityAirports.size shouldBe (DUMMY_POPULAR_AIRPORT[index] as FlightAirportModel).cityAirports.size
        }
    }

    @Test
    fun fetchAirportWithKeyword_fetchFailed_airportListShouldBeFail() {
        // given
        coEvery { flightAirportSuggestionUseCase.fetchAirportSuggestion(any()) } coAnswers { throw Throwable("Fetch Failed") }

        // when
        flightAirportPickerViewModel.fetchAirport("dummy airport")

        // then
        assert(flightAirportPickerViewModel.airportList.value is Fail)
        (flightAirportPickerViewModel.airportList.value as Fail).throwable.message shouldBe "Fetch Failed"
    }

    @Test
    fun fetchAirportWithKeyword_fetchSuccessButNoResult_airportListShouldBeSuccessButEmpty() {
        // given
        coEvery { flightAirportSuggestionUseCase.fetchAirportSuggestion(any()) } returns arrayListOf()

        // when
        flightAirportPickerViewModel.fetchAirport("dummy airport")

        // then
        assert(flightAirportPickerViewModel.airportList.value is Success)
        (flightAirportPickerViewModel.airportList.value as Success).data.size shouldBe 0
    }

    @Test
    fun fetchAirportWithKeyword_fetchSuccessAndContainsData_airportListShouldBeSuccessAndContainsData() {
        // given
        coEvery { flightAirportSuggestionUseCase.fetchAirportSuggestion(any()) } returns DUMMY_SUGGESTION_AIRPORT

        // when
        flightAirportPickerViewModel.fetchAirport("BTJ")

        // then
        assert(flightAirportPickerViewModel.airportList.value is Success)
        val airportList = (flightAirportPickerViewModel.airportList.value as Success).data
        airportList.size shouldBe DUMMY_SUGGESTION_AIRPORT.size

        for ((index, item) in airportList.withIndex()) {
            (item as FlightAirportModel).airportName shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).airportName
            item.airportCode shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).airportCode
            item.cityId shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).cityId
            item.cityCode shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).cityCode
            item.cityName shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).cityName
            item.countryName shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).countryName
            item.cityAirports.size shouldBe (DUMMY_SUGGESTION_AIRPORT[index] as FlightAirportModel).cityAirports.size
        }
    }

}