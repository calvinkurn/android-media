package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.usecase.GetFlightFareUseCase
import com.tokopedia.flight.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat
import java.util.*

/**
 * @author by furqan on 23/06/2020
 */
class FlightFareCalendarViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: FlightFareCalendarViewModel


    private val getFlightFareUseCase = mockk<GetFlightFareUseCase>()

    @Before
    fun setUp() {
        viewModel = FlightFareCalendarViewModel(dispatcherProvider, getFlightFareUseCase)
    }

    @Test
    fun getFareFlightCalendar_failedToFetch_flightFareCalendarDataShouldBeEmpty() {
        // given

        // when
        viewModel.getFareFlightCalendar(hashMapOf(), Date(), Date())

        // then
        viewModel.fareFlightCalendarData.value?.size shouldBe 0
    }

    @Test
    fun getFareFlightCalendar_successToFetchWithReturnFlightCalendar() {
        // given
        val dummyAttributes = arrayListOf(
                FlightFareAttributes("2021-11-11", 0, "Rp.0", "Rp.200", true),
                FlightFareAttributes("2021-12-12", 200, "Rp.200", "Rp.300", false)
        )
        coEvery { getFlightFareUseCase.executeOnBackground() } returns dummyAttributes
        coEvery { getFlightFareUseCase.createReturnParam(any()) } coAnswers {}
        coEvery { getFlightFareUseCase.params = any() } coAnswers {}
        coEvery { getFlightFareUseCase.minDate = any() } coAnswers {}
        coEvery { getFlightFareUseCase.maxDate = any() } coAnswers {}
        val dotFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("in", "id"))

        // when
        viewModel.getFareFlightCalendar(
                hashMapOf(),
                Date(),
                Date(),
                true,
                "2021-11-11"
        )

        // then
        val data = viewModel.fareFlightCalendarData.value!!
        data.size shouldBe dummyAttributes.size
        for (item in data) {
            val useIndex = if (item.dateFare == dummyAttributes[0].dateFare) 0 else if (item.dateFare == dummyAttributes[1].dateFare) 1 else 0
            item.dateFare shouldBe dummyAttributes[useIndex].dateFare
            item.cheapestPriceNumeric shouldBe ((dummyAttributes[useIndex].cheapestPriceNumeric + 0) / 1000)
            item.displayedFare shouldBe dotFormat.format(((dummyAttributes[useIndex].cheapestPriceNumeric + 0) / 1000))
        }
    }

    @Test
    fun getFareFlightCalendar_successToFetchWithReturnFlightCalendar_butWrongDate() {
        // given
        val dummyAttributes = arrayListOf(
                FlightFareAttributes("2021-11-11", 0, "Rp.0", "Rp.200", true),
                FlightFareAttributes("2021-12-12", 200, "Rp.200", "Rp.300", false)
        )
        coEvery { getFlightFareUseCase.executeOnBackground() } returns dummyAttributes
        coEvery { getFlightFareUseCase.createReturnParam(any()) } coAnswers {}
        coEvery { getFlightFareUseCase.params = any() } coAnswers {}
        coEvery { getFlightFareUseCase.minDate = any() } coAnswers {}
        coEvery { getFlightFareUseCase.maxDate = any() } coAnswers {}
        val dotFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("in", "id"))

        // when
        viewModel.getFareFlightCalendar(
                hashMapOf(),
                Date(),
                Date(),
                true,
                "2021-11-13"
        )

        // then
        viewModel.fareFlightCalendarData.value?.size shouldBe 0
    }

    @Test
    fun getFareFlightCalendar_successToFetchButEmpty() {
        // given
        coEvery { getFlightFareUseCase.executeOnBackground() } returns arrayListOf()
        coEvery { getFlightFareUseCase.createReturnParam(any()) } coAnswers {}
        coEvery { getFlightFareUseCase.params = any() } coAnswers {}
        coEvery { getFlightFareUseCase.minDate = any() } coAnswers {}
        coEvery { getFlightFareUseCase.maxDate = any() } coAnswers {}
        val dotFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("in", "id"))

        // when
        viewModel.getFareFlightCalendar(
                hashMapOf(),
                Date(),
                Date(),
                true,
                ""
        )

        // then
        viewModel.fareFlightCalendarData.value shouldBe null
    }
}