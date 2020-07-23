package com.tokopedia.flight.cancellationV2.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.utils.TravelTestDispatcherProvider
import com.tokopedia.flight.cancellationV2.domain.FlightCancellationGetPassengerUseCase
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_JOURNEY
import com.tokopedia.flight.dummy.DUMMY_CANCELLED_PASSENGER
import com.tokopedia.flight.dummy.DUMMY_EMPTY_PASSENGER_SELECTED_CANCELLATION
import com.tokopedia.flight.dummy.DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
import com.tokopedia.flight.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 23/07/2020
 */
class FlightCancellationPassengerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcherProvider = TravelTestDispatcherProvider()

    private val cancellationPassengerUseCase: FlightCancellationGetPassengerUseCase = mockk()

    private lateinit var viewModel: FlightCancellationPassengerViewModel

    @Before
    fun setUp() {
        viewModel = FlightCancellationPassengerViewModel(cancellationPassengerUseCase, testDispatcherProvider)
    }

    @Test
    fun init() {
        // given

        // when

        // then
        viewModel.selectedCancellationPassengerList.size shouldBe 0
        viewModel.canGoNext() shouldBe false
    }

    @Test
    fun canGoNext_withoutSelectedPassenger_shouldBeFalse() {
        // given
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_EMPTY_PASSENGER_SELECTED_CANCELLATION)

        // when
        val canGoNext = viewModel.canGoNext()

        // then
        canGoNext shouldBe false
    }

    @Test
    fun canGoNext_withSelectedPassenger_shouldBeFalse() {
        // given
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION)

        // when
        val canGoNext = viewModel.canGoNext()

        // then
        canGoNext shouldBe true
    }

    @Test
    fun getCancellablePassenger_failToFetch_valueShouldBeNull() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } coAnswers { throw Throwable() }

        // when
        viewModel.getCancellablePassenger("", arrayListOf())

        // then
        viewModel.cancellationPassengerList.value shouldBe null
    }

    @Test
    fun getCancellablePassenger_successToFetch() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION

        // when
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)

        // then
        viewModel.selectedCancellationPassengerList.size shouldBe 2
        for ((index, item) in viewModel.selectedCancellationPassengerList.withIndex()) {
            val swappedIndex = if (index == 1) 0 else 1
            item.invoiceId shouldBe "1234567890"
            item.passengerModelList.size shouldBe 0
            item.flightCancellationJourney.arrivalCity shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalCity
            item.flightCancellationJourney.departureCity shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureCity
            item.flightCancellationJourney.journeyId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.journeyId
            item.flightCancellationJourney.airlineIds.size shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.airlineIds.size
            item.flightCancellationJourney.airlineName shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.airlineName
            item.flightCancellationJourney.arrivalAirportId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalAirportId
            item.flightCancellationJourney.arrivalCityCode shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalCityCode
            item.flightCancellationJourney.arrivalTime shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalTime
            item.flightCancellationJourney.departureAirportId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureAirportId
            item.flightCancellationJourney.departureCityCode shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureCityCode
            item.flightCancellationJourney.departureTime shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureTime
            item.flightCancellationJourney.isRefundable shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.isRefundable
        }


        for ((index, item) in viewModel.cancellationPassengerList.value!!.withIndex()) {
            val swappedIndex = if (index == 1) 0 else 1
            item.invoiceId shouldBe "1234567890"
            item.passengerModelList.size shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].passengerModelList.size
            item.flightCancellationJourney.arrivalCity shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalCity
            item.flightCancellationJourney.departureCity shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureCity
            item.flightCancellationJourney.journeyId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.journeyId
            item.flightCancellationJourney.airlineIds.size shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.airlineIds.size
            item.flightCancellationJourney.airlineName shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.airlineName
            item.flightCancellationJourney.arrivalAirportId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalAirportId
            item.flightCancellationJourney.arrivalCityCode shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalCityCode
            item.flightCancellationJourney.arrivalTime shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.arrivalTime
            item.flightCancellationJourney.departureAirportId shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureAirportId
            item.flightCancellationJourney.departureCityCode shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureCityCode
            item.flightCancellationJourney.departureTime shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.departureTime
            item.flightCancellationJourney.isRefundable shouldBe DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[swappedIndex].flightCancellationJourney.isRefundable
        }
    }

    @Test
    fun checkPassenger_withRelations_shouldNotifyRelationChecked() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = 1

        // when
        val shouldNotify = viewModel.checkPassenger(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[0].passengerModelList[0], position)

        // then
        shouldNotify shouldBe true
        viewModel.selectedCancellationPassengerList[position].passengerModelList.size shouldBe 2
    }

    @Test
    fun uncheckPassenger_withRelations() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = 1
        val selectedPassenger = DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[0].passengerModelList[0]
        viewModel.checkPassenger(selectedPassenger, position)

        // when
        viewModel.uncheckPassenger(selectedPassenger, position)

        // then
        viewModel.selectedCancellationPassengerList[position].passengerModelList.size shouldBe 0
    }

    @Test
    fun isPassengerChecked_withoutCheckedPassenger_shouldBeFalse() {
        // given
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION)

        // when
        val isPassengerChecked = viewModel.isPassengerChecked(FlightCancellationPassengerModel())

        // then
        isPassengerChecked shouldBe false
    }

    @Test
    fun isPassengerChecked_cancelledPassenger_shouldBeTrue() {
        // given
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION)
        val selectedPassengerModel = DUMMY_CANCELLED_PASSENGER

        // when
        val isPassengerChecked = viewModel.isPassengerChecked(selectedPassengerModel)

        // then
        isPassengerChecked shouldBe true
    }

    @Test
    fun isPassengerChecked_selectedPassenger_shouldBeTrue() {
        // given
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION)
        val selectedPassengerModel = DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[0].passengerModelList[0]

        // when
        val isPassengerChecked = viewModel.isPassengerChecked(selectedPassengerModel)

        // then
        isPassengerChecked shouldBe true
    }
}