package com.tokopedia.flight.cancellationdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.dummy.DUMMY_CANCELLATION_LIST_DATA
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val orderDetailUseCase: FlightOrderDetailUseCase = mockk()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: FlightOrderCancellationListViewModel

    @Before
    fun setUp() {
        viewModel = FlightOrderCancellationListViewModel(
                orderDetailUseCase,
                testDispatcherProvider
        )
    }

    @Test
    fun onInitViewModelShouldContainEmptyOrderId() {
        // given

        // when
        viewModel.fetchCancellationData()

        // then
        viewModel.orderId shouldBe ""
        viewModel.cancellationList.value shouldBe null
    }

    @Test
    fun onFetchCancellationData_errorFetch_cancellationListShouldBeError() {
        // given
        val throwable = Throwable("Failed to Fetch")
        coEvery { orderDetailUseCase.executeGetCancellationList(any()) } coAnswers { throw throwable }
        viewModel.orderId = "dummy order id"

        // when
        viewModel.fetchCancellationData()

        // then
        assert(viewModel.cancellationList.value is Fail)
        (viewModel.cancellationList.value as Fail).throwable.message shouldBe throwable.message
    }

    @Test
    fun onFetchCancellationData_successFetch_cancellationListShouldBeDummyData() {
        // given
        val dummyData = DUMMY_CANCELLATION_LIST_DATA
        viewModel.orderId = "dummy order id"
        coEvery { orderDetailUseCase.executeGetCancellationList(any()) } returns dummyData

        // when
        viewModel.fetchCancellationData()

        // then
        assert(viewModel.cancellationList.value is Success)
        val data = (viewModel.cancellationList.value as Success).data

        data.size shouldBe dummyData.size
        for ((index, item) in data.withIndex()) {
            item.orderId shouldBe dummyData[index].orderId
            item.cancellationDetail.refundId shouldBe dummyData[index].cancellationDetail.refundId
            item.cancellationDetail.createTime shouldBe dummyData[index].cancellationDetail.createTime
            item.cancellationDetail.realRefund shouldBe dummyData[index].cancellationDetail.realRefund
            item.cancellationDetail.status shouldBe dummyData[index].cancellationDetail.status
            item.cancellationDetail.statusStr shouldBe dummyData[index].cancellationDetail.statusStr
            item.cancellationDetail.statusType shouldBe dummyData[index].cancellationDetail.statusType
            item.cancellationDetail.refundInfo shouldBe dummyData[index].cancellationDetail.refundInfo
            item.cancellationDetail.passengers.size shouldBe dummyData[index].cancellationDetail.passengers.size
            for ((indexPassenger, passenger) in item.cancellationDetail.passengers.withIndex()) {
                passenger.id shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].id
                passenger.type shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].type
                passenger.typeString shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].typeString
                passenger.title shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].title
                passenger.titleString shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].titleString
                passenger.firstName shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].firstName
                passenger.lastName shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].lastName
                passenger.departureAirportId shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].departureAirportId
                passenger.arrivalAirportId shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].arrivalAirportId
                passenger.journeyId shouldBe dummyData[index].cancellationDetail.passengers[indexPassenger].journeyId
            }
            item.cancellationDetail.journeys.size shouldBe dummyData[index].cancellationDetail.journeys.size
            for ((indexJourney, journey) in item.cancellationDetail.journeys.withIndex()) {
                journey.id shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].id
                journey.status shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].status
                journey.departureId shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].departureId
                journey.departureTime shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].departureTime
                journey.departureAirportName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].departureAirportName
                journey.departureCityName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].departureCityName
                journey.arrivalId shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].arrivalId
                journey.arrivalTime shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].arrivalTime
                journey.arrivalAirportName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].arrivalAirportName
                journey.arrivalCityName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].arrivalCityName
                journey.totalTransit shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].totalTransit
                journey.totalStop shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].totalStop
                journey.addDayArrival shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].addDayArrival
                journey.duration shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].duration
                journey.durationMinute shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].durationMinute
                journey.fare.adultNumeric shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].fare.adultNumeric
                journey.fare.childNumeric shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].fare.childNumeric
                journey.fare.infantNumeric shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].fare.infantNumeric
                journey.routes.size shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].routes.size
                journey.webCheckIn.title shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.title
                journey.webCheckIn.subtitle shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.subtitle
                journey.webCheckIn.startTime shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.startTime
                journey.webCheckIn.endTime shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.endTime
                journey.webCheckIn.iconUrl shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.iconUrl
                journey.webCheckIn.appUrl shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.appUrl
                journey.webCheckIn.webUrl shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].webCheckIn.webUrl
                journey.airlineName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].airlineLogo
                journey.airlineName shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].airlineName
                journey.refundableInfo shouldBe dummyData[index].cancellationDetail.journeys[indexJourney].refundableInfo
            }
        }
    }

}