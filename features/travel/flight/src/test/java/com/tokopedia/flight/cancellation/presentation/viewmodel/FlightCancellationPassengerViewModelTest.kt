package com.tokopedia.flight.cancellation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.flight.cancellation.domain.FlightCancellationGetPassengerUseCase
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_JOURNEY
import com.tokopedia.flight.dummy.DUMMY_CANCELLED_PASSENGER
import com.tokopedia.flight.dummy.DUMMY_EMPTY_PASSENGER_SELECTED_CANCELLATION
import com.tokopedia.flight.dummy.DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
import com.tokopedia.flight.shouldBe
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private val userSession: UserSessionInterface = mockk()
    private val cancellationPassengerUseCase: FlightCancellationGetPassengerUseCase = mockk()

    private lateinit var viewModel: FlightCancellationPassengerViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightCancellationPassengerViewModel(cancellationPassengerUseCase, flightAnalytics, userSession, testDispatcherProvider)
    }

    @Test
    fun init() {
        // given

        // when

        // then
        viewModel.selectedCancellationPassengerList.size shouldBe 0
        viewModel.canGoNext() shouldBe false
        viewModel.invoiceId shouldBe ""
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
    fun canGoNext_withSelectedPassenger_shouldBeTrue() {
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
    fun checkPassenger_wrongPosition_shouldNotNotifyRelationChecked() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = -1

        // when
        val shouldNotify = viewModel.checkPassenger(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[0].passengerModelList[0], position)

        // then
        shouldNotify shouldBe false
        viewModel.selectedCancellationPassengerList[1].passengerModelList.size shouldBe 0
    }

    @Test
    fun checkPassenger_checkSamePassengerMultipleTime() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = 0
        viewModel.checkPassenger(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[1].passengerModelList[0], position)

        // when
        viewModel.checkPassenger(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[1].passengerModelList[0], position)

        // then
        viewModel.selectedCancellationPassengerList[position].passengerModelList.size shouldBe 1
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
    fun uncheckPassenger_wrongPosition_shouldDoNothing() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = -1
        val selectedPassenger = DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[0].passengerModelList[0]
        viewModel.checkPassenger(selectedPassenger, 1)

        // when
        viewModel.uncheckPassenger(selectedPassenger, position)

        // then
        viewModel.selectedCancellationPassengerList[1].passengerModelList.size shouldBe 2
    }

    @Test
    fun uncheckPassenger_emptyRelation() {
        // given
        coEvery { cancellationPassengerUseCase.fetchCancellablePassenger(any()) } returns DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION
        viewModel.getCancellablePassenger("1234567890", DUMMY_CANCELLATION_JOURNEY)
        val position = 0
        val selectedPassenger = DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION[1].passengerModelList[0]
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

    @Test
    fun trackOnNext() {
        // given
        viewModel.invoiceId = "1234567890"
        viewModel.selectedCancellationPassengerList.addAll(DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION)
        coEvery { userSession.userId } returns "0987654321"

        // when
        viewModel.trackOnNext()

        // then
        for (item in viewModel.selectedCancellationPassengerList) {
            val route = "${item.flightCancellationJourney.departureAirportId}${item.flightCancellationJourney.arrivalAirportId}"
            val departureDate = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, item.flightCancellationJourney.departureTime)

            coVerify {
                flightAnalytics.eventClickNextOnCancellationPassenger(
                        "$route - ${item.flightCancellationJourney.airlineName} - $departureDate - 1234567890",
                        "0987654321"
                )
            }
        }
    }
}