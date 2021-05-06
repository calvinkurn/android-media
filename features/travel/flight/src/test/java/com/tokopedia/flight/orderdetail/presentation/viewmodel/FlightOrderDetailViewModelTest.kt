package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.dummy.DUMMY_CROSS_SELL
import com.tokopedia.flight.dummy.DUMMY_FAILED_ORDER_DETAIL_DATA
import com.tokopedia.flight.dummy.DUMMY_ORDER_DETAIL_DATA
import com.tokopedia.flight.dummy.DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailGetInvoiceEticketUseCase
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.*
import com.tokopedia.flight.orderdetail.presentation.model.mapper.FlightOrderDetailCancellationMapper
import com.tokopedia.flight.shouldBe
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * @author by furqan on 22/10/2020
 */
class FlightOrderDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val userSession: UserSessionInterface = mockk()
    private val useCase: FlightOrderDetailUseCase = mockk()
    private val getInvoiceEticketUseCase: FlightOrderDetailGetInvoiceEticketUseCase = mockk()
    private val crossSellUseCase: TravelCrossSellingUseCase = mockk()
    private val mapper: FlightOrderDetailCancellationMapper = FlightOrderDetailCancellationMapper()
    private val testDispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: FlightOrderDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = FlightOrderDetailViewModel(
                userSession,
                useCase,
                getInvoiceEticketUseCase,
                crossSellUseCase,
                mapper,
                flightAnalytics,
                testDispatcherProvider)
    }

    @Test
    fun userEmail_whenNotLoggedIn_shouldBeEmptyString() {
        // given
        coEvery { userSession.isLoggedIn } returns false

        // when
        val userEmail = viewModel.getUserEmail()

        // then
        userEmail shouldBe ""
    }

    @Test
    fun userEmail_whenLoggedIn_shouldBeUserEmail() {
        // given
        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.email } returns "nakama@tokopedia.com"

        // when
        val userEmail = viewModel.getUserEmail()

        // then
        userEmail shouldBe "nakama@tokopedia.com"
    }

    @Test
    fun fetchOrderDetailData_errorFetching_shouldReturnFail() {
        // given
        coEvery { useCase.execute(any(), any()) } coAnswers { throw MessageErrorException("Error Fetch Data") }
        viewModel.orderId = "1234567890"

        // when
        viewModel.fetchOrderDetailData()

        // then
        assert(viewModel.orderDetailData.value is Fail)
        (viewModel.orderDetailData.value as Fail).throwable.message shouldBe "Error Fetch Data"
    }

    @Test
    fun fetchOrderDetailData_successFetching_shouldReturnSuccessWithData() {
        // given
        coEvery { useCase.execute(any(), any()) } returns DUMMY_ORDER_DETAIL_DATA
        viewModel.orderId = "1234567890"

        // when
        viewModel.fetchOrderDetailData()

        // then
        assert(viewModel.orderDetailData.value is Success)
        val data = viewModel.orderDetailData.value as Success

        data.data.omsId shouldBe DUMMY_ORDER_DETAIL_DATA.omsId
        data.data.createTime shouldBe DUMMY_ORDER_DETAIL_DATA.createTime
        data.data.status shouldBe DUMMY_ORDER_DETAIL_DATA.status
        data.data.statusString shouldBe DUMMY_ORDER_DETAIL_DATA.statusString
        data.data.id shouldBe DUMMY_ORDER_DETAIL_DATA.id
        data.data.invoiceId shouldBe DUMMY_ORDER_DETAIL_DATA.invoiceId
        data.data.contactName shouldBe DUMMY_ORDER_DETAIL_DATA.contactName
        data.data.email shouldBe DUMMY_ORDER_DETAIL_DATA.email
        data.data.phone shouldBe DUMMY_ORDER_DETAIL_DATA.phone
        data.data.countryId shouldBe DUMMY_ORDER_DETAIL_DATA.countryId
        data.data.totalAdult shouldBe DUMMY_ORDER_DETAIL_DATA.totalAdult
        data.data.totalAdultNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.totalAdultNumeric
        data.data.totalChild shouldBe DUMMY_ORDER_DETAIL_DATA.totalChild
        data.data.totalChildNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.totalChildNumeric
        data.data.totalInfant shouldBe DUMMY_ORDER_DETAIL_DATA.totalInfant
        data.data.totalInfantNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.totalInfantNumeric
        data.data.totalPrice shouldBe DUMMY_ORDER_DETAIL_DATA.totalPrice
        data.data.totalPriceNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.totalPriceNumeric
        data.data.currency shouldBe DUMMY_ORDER_DETAIL_DATA.currency
        data.data.pdf shouldBe DUMMY_ORDER_DETAIL_DATA.pdf
        data.data.isDomestic shouldBe DUMMY_ORDER_DETAIL_DATA.isDomestic
        data.data.mandatoryDob shouldBe DUMMY_ORDER_DETAIL_DATA.mandatoryDob
        data.data.classText shouldBe DUMMY_ORDER_DETAIL_DATA.classText
        data.data.contactUsURL shouldBe DUMMY_ORDER_DETAIL_DATA.contactUsURL
        data.data.payment.id shouldBe DUMMY_ORDER_DETAIL_DATA.payment.id
        data.data.payment.status shouldBe DUMMY_ORDER_DETAIL_DATA.payment.status
        data.data.payment.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.statusStr
        data.data.payment.gatewayName shouldBe DUMMY_ORDER_DETAIL_DATA.payment.gatewayName
        data.data.payment.gatewayIcon shouldBe DUMMY_ORDER_DETAIL_DATA.payment.gatewayIcon
        data.data.payment.paymentDate shouldBe DUMMY_ORDER_DETAIL_DATA.payment.paymentDate
        data.data.payment.expireOn shouldBe DUMMY_ORDER_DETAIL_DATA.payment.expireOn
        data.data.payment.transactionCode shouldBe DUMMY_ORDER_DETAIL_DATA.payment.transactionCode
        data.data.payment.promoCode shouldBe DUMMY_ORDER_DETAIL_DATA.payment.promoCode
        data.data.payment.adminFeeAmount shouldBe DUMMY_ORDER_DETAIL_DATA.payment.adminFeeAmount
        data.data.payment.adminFeeAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.adminFeeAmountStr
        data.data.payment.voucherAmount shouldBe DUMMY_ORDER_DETAIL_DATA.payment.voucherAmount
        data.data.payment.voucherAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.voucherAmountStr
        data.data.payment.saldoAmount shouldBe DUMMY_ORDER_DETAIL_DATA.payment.saldoAmount
        data.data.payment.saldoAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.saldoAmountStr
        data.data.payment.totalAmount shouldBe DUMMY_ORDER_DETAIL_DATA.payment.totalAmount
        data.data.payment.totalAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.totalAmountStr
        data.data.payment.needToPayAmount shouldBe DUMMY_ORDER_DETAIL_DATA.payment.needToPayAmount
        data.data.payment.needToPayAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA.payment.needToPayAmountStr
        data.data.payment.uniqueCode shouldBe DUMMY_ORDER_DETAIL_DATA.payment.uniqueCode
        data.data.payment.accountBankName shouldBe DUMMY_ORDER_DETAIL_DATA.payment.accountBankName
        data.data.payment.accountBranch shouldBe DUMMY_ORDER_DETAIL_DATA.payment.accountBranch
        data.data.payment.accountName shouldBe DUMMY_ORDER_DETAIL_DATA.payment.accountName
        data.data.payment.total shouldBe DUMMY_ORDER_DETAIL_DATA.payment.total
        data.data.journeys.size shouldBe DUMMY_ORDER_DETAIL_DATA.journeys.size
        for ((index, item) in data.data.journeys.withIndex()) {
            item.id shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].id
            item.status shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].status
            item.departureId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].departureId
            item.departureTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].departureTime
            item.departureAirportName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].departureAirportName
            item.departureCityName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].departureCityName
            item.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].arrivalId
            item.arrivalTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].arrivalTime
            item.arrivalAirportName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].arrivalAirportName
            item.arrivalCityName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].arrivalCityName
            item.totalTransit shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].totalTransit
            item.totalStop shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].totalStop
            item.addDayArrival shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].addDayArrival
            item.duration shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].duration
            item.durationMinute shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].durationMinute
            item.fare.adultNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].fare.adultNumeric
            item.fare.childNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].fare.childNumeric
            item.fare.infantNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].fare.infantNumeric
            item.routes.size shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes.size
            for ((routeIndex, route) in item.routes.withIndex()) {
                route.departureId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].departureId
                route.departureTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].departureTime
                route.departureAirportName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].departureAirportName
                route.departureCityName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].departureCityName
                route.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].arrivalId
                route.arrivalTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].arrivalTime
                route.arrivalAirportName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].arrivalAirportName
                route.arrivalCityName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].arrivalCityName
                route.pnr shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].pnr
                route.airlineId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].airlineId
                route.airlineName shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].airlineName
                route.airlineLogo shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].airlineLogo
                route.operatorAirlineId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].operatorAirlineId
                route.flightNumber shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].flightNumber
                route.duration shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].duration
                route.durationMinute shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].durationMinute
                route.layover shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].layover
                route.layoverMinute shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].layoverMinute
                route.refundable shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].refundable
                route.departureTerminal shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].departureTerminal
                route.arrivalTerminal shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].arrivalTerminal
                route.stop shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].stop
                route.carrier shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].carrier
                route.stopDetails.size shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].stopDetails.size
                for ((stopIndex, stopItem) in route.stopDetails.withIndex()) {
                    stopItem shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].stopDetails[stopIndex]
                }
                route.ticketNumbers.size shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].ticketNumbers.size
                for ((ticketIndex, ticketItem) in route.ticketNumbers.withIndex()) {
                    ticketItem.passengerId shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].ticketNumbers[ticketIndex].passengerId
                    ticketItem.ticketNumber shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].ticketNumbers[ticketIndex].ticketNumber
                }
                route.freeAmenities.cabinBaggage.isUpTo shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.isUpTo
                route.freeAmenities.cabinBaggage.unit shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.unit
                route.freeAmenities.cabinBaggage.value shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.value
                route.freeAmenities.freeBaggage.isUpTo shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.isUpTo
                route.freeAmenities.freeBaggage.unit shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.unit
                route.freeAmenities.freeBaggage.value shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.value
                route.freeAmenities.meal shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.meal
                route.freeAmenities.usbPort shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.usbPort
                route.freeAmenities.wifi shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.wifi
                route.freeAmenities.others.size shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.others.size
                for ((otherIndex, other) in route.freeAmenities.others.withIndex()) {
                    other shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].routes[routeIndex].freeAmenities.others[otherIndex]
                }
            }
            item.webCheckIn.title shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.title
            item.webCheckIn.subtitle shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.subtitle
            item.webCheckIn.startTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.startTime
            item.webCheckIn.endTime shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.endTime
            item.webCheckIn.iconUrl shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.iconUrl
            item.webCheckIn.appUrl shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.appUrl
            item.webCheckIn.webUrl shouldBe DUMMY_ORDER_DETAIL_DATA.journeys[index].webCheckIn.webUrl
        }
        data.data.passengers.size shouldBe DUMMY_ORDER_DETAIL_DATA.passengers.size
        for ((passengerIndex, passenger) in data.data.passengers.withIndex()) {
            passenger.id shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].id
            passenger.type shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].type
            passenger.title shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].title
            passenger.firstName shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].firstName
            passenger.lastName shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].lastName
            passenger.dob shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].dob
            passenger.nationality shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].nationality
            passenger.passportNo shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].passportNo
            passenger.passportCountry shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].passportCountry
            passenger.passportExpiry shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].passportExpiry
            passenger.amenities.size shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities.size
            for ((amenityIndex, amenity) in passenger.amenities.withIndex()) {
                amenity.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].arrivalId
                amenity.departureId shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].departureId
                amenity.detail shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].detail
                amenity.price shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].price
                amenity.priceNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].priceNumeric
                amenity.type shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].amenities[amenityIndex].type
            }
            passenger.cancelStatus.size shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].cancelStatus.size
            for ((cancelIndex, cancel) in passenger.cancelStatus.withIndex()) {
                cancel.status shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].cancelStatus[cancelIndex].status
                cancel.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].cancelStatus[cancelIndex].statusStr
                cancel.statusType shouldBe DUMMY_ORDER_DETAIL_DATA.passengers[passengerIndex].cancelStatus[cancelIndex].statusType
            }
        }
        data.data.actionButtons.size shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons.size
        for ((actionIndex, action) in data.data.actionButtons.withIndex()) {
            action.id shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].id
            action.label shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].label
            action.buttonType shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].buttonType
            action.uri shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].uri
            action.uriWeb shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].uriWeb
            action.mappingUrl shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].mappingUrl
            action.method shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].method
            action.weight shouldBe DUMMY_ORDER_DETAIL_DATA.actionButtons[actionIndex].weight
        }
        data.data.conditionalInfos.size shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos.size
        for ((infoIndex, info) in data.data.conditionalInfos.withIndex()) {
            info.type shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos[infoIndex].type
            info.title shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos[infoIndex].title
            info.text shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos[infoIndex].text
            info.border shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos[infoIndex].border
            info.background shouldBe DUMMY_ORDER_DETAIL_DATA.conditionalInfos[infoIndex].background
        }
        data.data.insurances.size shouldBe DUMMY_ORDER_DETAIL_DATA.insurances.size
        for ((insuranceIndex, insurance) in data.data.insurances.withIndex()) {
            insurance.id shouldBe DUMMY_ORDER_DETAIL_DATA.insurances[insuranceIndex].id
            insurance.title shouldBe DUMMY_ORDER_DETAIL_DATA.insurances[insuranceIndex].title
            insurance.tagline shouldBe DUMMY_ORDER_DETAIL_DATA.insurances[insuranceIndex].tagline
            insurance.paidAmount shouldBe DUMMY_ORDER_DETAIL_DATA.insurances[insuranceIndex].paidAmount
            insurance.paidAmountNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.insurances[insuranceIndex].paidAmountNumeric
        }
        data.data.cancellations.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations.size
        for ((cancelIndex, cancel) in data.data.cancellations.withIndex()) {
            cancel.cancelId shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].cancelId
            for ((cancelDetailIndex, cancelDetail) in cancel.cancelDetails.withIndex()) {
                cancelDetail.journeyId shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].journeyId
                cancelDetail.passengerId shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].passengerId
                cancelDetail.refundedGateway shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].refundedGateway
                cancelDetail.refundedTime shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].refundedTime
            }
            cancel.createTime shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].createTime
            cancel.updateTime shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].updateTime
            cancel.estimatedRefund shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].estimatedRefund
            cancel.estimatedRefundNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].estimatedRefundNumeric
            cancel.realRefund shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].realRefund
            cancel.realRefundNumeric shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].realRefundNumeric
            cancel.status shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].status
            cancel.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].statusStr
            cancel.statusType shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].statusType
            cancel.refundInfo shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundInfo
            cancel.refundDetail.topInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.topInfo.size
            for ((topIndex, top) in cancel.refundDetail.topInfo.withIndex()) {
                top.key shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.topInfo[topIndex].key
                top.value shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.topInfo[topIndex].value
            }
            cancel.refundDetail.middleInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.middleInfo.size
            for ((middleIndex, middle) in cancel.refundDetail.middleInfo.withIndex()) {
                middle.title shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].title
                middle.content.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content.size
                for ((contentIndex, content) in middle.content.withIndex()) {
                    content.key shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content[contentIndex].key
                    content.value shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content[contentIndex].value
                }
            }
            cancel.refundDetail.bottomInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.bottomInfo.size
            for ((bottomIndex, bottom) in cancel.refundDetail.bottomInfo.withIndex()) {
                bottom.key shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.bottomInfo[bottomIndex].key
                bottom.value shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.bottomInfo[bottomIndex].value
            }
            cancel.refundDetail.notes.size shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.notes.size
            for ((noteIndex, note) in cancel.refundDetail.notes.withIndex()) {
                note shouldBe DUMMY_ORDER_DETAIL_DATA.cancellations[cancelIndex].refundDetail.notes[noteIndex]
            }
        }
    }

    @Test
    fun fetchOrderDetailData_successFetchingMultiAirline_shouldReturnSuccessWithData() {
        // given
        coEvery { useCase.execute(any(), any()) } returns DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE
        viewModel.orderId = "1234567890"

        // when
        viewModel.fetchOrderDetailData()

        // then
        assert(viewModel.orderDetailData.value is Success)
        val data = viewModel.orderDetailData.value as Success

        data.data.omsId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.omsId
        data.data.createTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.createTime
        data.data.status shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.status
        data.data.statusString shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.statusString
        data.data.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.id
        data.data.invoiceId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.invoiceId
        data.data.contactName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.contactName
        data.data.email shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.email
        data.data.phone shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.phone
        data.data.countryId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.countryId
        data.data.totalAdult shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalAdult
        data.data.totalAdultNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalAdultNumeric
        data.data.totalChild shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalChild
        data.data.totalChildNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalChildNumeric
        data.data.totalInfant shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalInfant
        data.data.totalInfantNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalInfantNumeric
        data.data.totalPrice shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalPrice
        data.data.totalPriceNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.totalPriceNumeric
        data.data.currency shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.currency
        data.data.pdf shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.pdf
        data.data.isDomestic shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.isDomestic
        data.data.mandatoryDob shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.mandatoryDob
        data.data.classText shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.classText
        data.data.contactUsURL shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.contactUsURL
        data.data.payment.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.id
        data.data.payment.status shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.status
        data.data.payment.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.statusStr
        data.data.payment.gatewayName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.gatewayName
        data.data.payment.gatewayIcon shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.gatewayIcon
        data.data.payment.paymentDate shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.paymentDate
        data.data.payment.expireOn shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.expireOn
        data.data.payment.transactionCode shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.transactionCode
        data.data.payment.promoCode shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.promoCode
        data.data.payment.adminFeeAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.adminFeeAmount
        data.data.payment.adminFeeAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.adminFeeAmountStr
        data.data.payment.voucherAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.voucherAmount
        data.data.payment.voucherAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.voucherAmountStr
        data.data.payment.saldoAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.saldoAmount
        data.data.payment.saldoAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.saldoAmountStr
        data.data.payment.totalAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.totalAmount
        data.data.payment.totalAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.totalAmountStr
        data.data.payment.needToPayAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.needToPayAmount
        data.data.payment.needToPayAmountStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.needToPayAmountStr
        data.data.payment.uniqueCode shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.uniqueCode
        data.data.payment.accountBankName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.accountBankName
        data.data.payment.accountBranch shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.accountBranch
        data.data.payment.accountName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.accountName
        data.data.payment.total shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.payment.total
        data.data.journeys.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys.size
        for ((index, item) in data.data.journeys.withIndex()) {
            item.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].id
            item.status shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].status
            item.departureId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].departureId
            item.departureTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].departureTime
            item.departureAirportName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].departureAirportName
            item.departureCityName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].departureCityName
            item.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].arrivalId
            item.arrivalTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].arrivalTime
            item.arrivalAirportName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].arrivalAirportName
            item.arrivalCityName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].arrivalCityName
            item.totalTransit shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].totalTransit
            item.totalStop shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].totalStop
            item.addDayArrival shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].addDayArrival
            item.duration shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].duration
            item.durationMinute shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].durationMinute
            item.fare.adultNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].fare.adultNumeric
            item.fare.childNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].fare.childNumeric
            item.fare.infantNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].fare.infantNumeric
            item.routes.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes.size
            for ((routeIndex, route) in item.routes.withIndex()) {
                route.departureId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].departureId
                route.departureTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].departureTime
                route.departureAirportName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].departureAirportName
                route.departureCityName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].departureCityName
                route.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].arrivalId
                route.arrivalTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].arrivalTime
                route.arrivalAirportName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].arrivalAirportName
                route.arrivalCityName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].arrivalCityName
                route.pnr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].pnr
                route.airlineId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].airlineId
                route.airlineName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].airlineName
                route.airlineLogo shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].airlineLogo
                route.operatorAirlineId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].operatorAirlineId
                route.flightNumber shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].flightNumber
                route.duration shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].duration
                route.durationMinute shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].durationMinute
                route.layover shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].layover
                route.layoverMinute shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].layoverMinute
                route.refundable shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].refundable
                route.departureTerminal shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].departureTerminal
                route.arrivalTerminal shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].arrivalTerminal
                route.stop shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].stop
                route.carrier shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].carrier
                route.stopDetails.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].stopDetails.size
                for ((stopIndex, stopItem) in route.stopDetails.withIndex()) {
                    stopItem shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].stopDetails[stopIndex]
                }
                route.ticketNumbers.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].ticketNumbers.size
                for ((ticketIndex, ticketItem) in route.ticketNumbers.withIndex()) {
                    ticketItem.passengerId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].ticketNumbers[ticketIndex].passengerId
                    ticketItem.ticketNumber shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].ticketNumbers[ticketIndex].ticketNumber
                }
                route.freeAmenities.cabinBaggage.isUpTo shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.isUpTo
                route.freeAmenities.cabinBaggage.unit shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.unit
                route.freeAmenities.cabinBaggage.value shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.cabinBaggage.value
                route.freeAmenities.freeBaggage.isUpTo shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.isUpTo
                route.freeAmenities.freeBaggage.unit shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.unit
                route.freeAmenities.freeBaggage.value shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.freeBaggage.value
                route.freeAmenities.meal shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.meal
                route.freeAmenities.usbPort shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.usbPort
                route.freeAmenities.wifi shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.wifi
                route.freeAmenities.others.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.others.size
                for ((otherIndex, other) in route.freeAmenities.others.withIndex()) {
                    other shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].routes[routeIndex].freeAmenities.others[otherIndex]
                }
            }
            item.webCheckIn.title shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.title
            item.webCheckIn.subtitle shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.subtitle
            item.webCheckIn.startTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.startTime
            item.webCheckIn.endTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.endTime
            item.webCheckIn.iconUrl shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.iconUrl
            item.webCheckIn.appUrl shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.appUrl
            item.webCheckIn.webUrl shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.journeys[index].webCheckIn.webUrl
        }
        data.data.passengers.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers.size
        for ((passengerIndex, passenger) in data.data.passengers.withIndex()) {
            passenger.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].id
            passenger.type shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].type
            passenger.title shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].title
            passenger.firstName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].firstName
            passenger.lastName shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].lastName
            passenger.dob shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].dob
            passenger.nationality shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].nationality
            passenger.passportNo shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].passportNo
            passenger.passportCountry shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].passportCountry
            passenger.passportExpiry shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].passportExpiry
            passenger.amenities.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities.size
            for ((amenityIndex, amenity) in passenger.amenities.withIndex()) {
                amenity.arrivalId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].arrivalId
                amenity.departureId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].departureId
                amenity.detail shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].detail
                amenity.price shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].price
                amenity.priceNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].priceNumeric
                amenity.type shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].amenities[amenityIndex].type
            }
            passenger.cancelStatus.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].cancelStatus.size
            for ((cancelIndex, cancel) in passenger.cancelStatus.withIndex()) {
                cancel.status shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].cancelStatus[cancelIndex].status
                cancel.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].cancelStatus[cancelIndex].statusStr
                cancel.statusType shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.passengers[passengerIndex].cancelStatus[cancelIndex].statusType
            }
        }
        data.data.actionButtons.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons.size
        for ((actionIndex, action) in data.data.actionButtons.withIndex()) {
            action.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].id
            action.label shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].label
            action.buttonType shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].buttonType
            action.uri shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].uri
            action.uriWeb shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].uriWeb
            action.mappingUrl shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].mappingUrl
            action.method shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].method
            action.weight shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.actionButtons[actionIndex].weight
        }
        data.data.conditionalInfos.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos.size
        for ((infoIndex, info) in data.data.conditionalInfos.withIndex()) {
            info.type shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos[infoIndex].type
            info.title shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos[infoIndex].title
            info.text shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos[infoIndex].text
            info.border shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos[infoIndex].border
            info.background shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.conditionalInfos[infoIndex].background
        }
        data.data.insurances.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances.size
        for ((insuranceIndex, insurance) in data.data.insurances.withIndex()) {
            insurance.id shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances[insuranceIndex].id
            insurance.title shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances[insuranceIndex].title
            insurance.tagline shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances[insuranceIndex].tagline
            insurance.paidAmount shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances[insuranceIndex].paidAmount
            insurance.paidAmountNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.insurances[insuranceIndex].paidAmountNumeric
        }
        data.data.cancellations.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations.size
        for ((cancelIndex, cancel) in data.data.cancellations.withIndex()) {
            cancel.cancelId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].cancelId
            for ((cancelDetailIndex, cancelDetail) in cancel.cancelDetails.withIndex()) {
                cancelDetail.journeyId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].journeyId
                cancelDetail.passengerId shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].passengerId
                cancelDetail.refundedGateway shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].refundedGateway
                cancelDetail.refundedTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].cancelDetails[cancelDetailIndex].refundedTime
            }
            cancel.createTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].createTime
            cancel.updateTime shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].updateTime
            cancel.estimatedRefund shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].estimatedRefund
            cancel.estimatedRefundNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].estimatedRefundNumeric
            cancel.realRefund shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].realRefund
            cancel.realRefundNumeric shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].realRefundNumeric
            cancel.status shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].status
            cancel.statusStr shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].statusStr
            cancel.statusType shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].statusType
            cancel.refundInfo shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundInfo
            cancel.refundDetail.topInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.topInfo.size
            for ((topIndex, top) in cancel.refundDetail.topInfo.withIndex()) {
                top.key shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.topInfo[topIndex].key
                top.value shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.topInfo[topIndex].value
            }
            cancel.refundDetail.middleInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.middleInfo.size
            for ((middleIndex, middle) in cancel.refundDetail.middleInfo.withIndex()) {
                middle.title shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].title
                middle.content.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content.size
                for ((contentIndex, content) in middle.content.withIndex()) {
                    content.key shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content[contentIndex].key
                    content.value shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.middleInfo[middleIndex].content[contentIndex].value
                }
            }
            cancel.refundDetail.bottomInfo.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.bottomInfo.size
            for ((bottomIndex, bottom) in cancel.refundDetail.bottomInfo.withIndex()) {
                bottom.key shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.bottomInfo[bottomIndex].key
                bottom.value shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.bottomInfo[bottomIndex].value
            }
            cancel.refundDetail.notes.size shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.notes.size
            for ((noteIndex, note) in cancel.refundDetail.notes.withIndex()) {
                note shouldBe DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE.cancellations[cancelIndex].refundDetail.notes[noteIndex]
            }
        }
    }

    @Test
    fun fetchEticketData_failedToFetch() {
        // given
        coEvery { getInvoiceEticketUseCase.executeGetETicket(any()) } coAnswers { throw Throwable("error") }

        // when
        viewModel.fetchETicketData()

        // then
        assert(viewModel.eticketData.value is Fail)
        (viewModel.eticketData.value as Fail).throwable.message shouldBe "error"
    }

    @Test
    fun fetchEticketData_successToFetch() {
        // given
        coEvery { getInvoiceEticketUseCase.executeGetETicket(any()) } returns "<html><body>SUCCESS</body></html>"

        // when
        viewModel.fetchETicketData()

        // then
        assert(viewModel.eticketData.value is Success)
        (viewModel.eticketData.value as Success).data shouldBe "<html><body>SUCCESS</body></html>"
    }

    @Test
    fun fetchCrossSellingData() {
        // given
        coEvery { crossSellUseCase.execute(any(), any(), any()) } returns DUMMY_CROSS_SELL

        // when
        viewModel.fetchCrossSellData()

        // then
        assert(viewModel.crossSell.value is Success)
        val dataCrossSell = (viewModel.crossSell.value as Success).data
        dataCrossSell.meta.title shouldBe DUMMY_CROSS_SELL.data.meta.title
        dataCrossSell.meta.uri shouldBe DUMMY_CROSS_SELL.data.meta.uri
        dataCrossSell.meta.uriWeb shouldBe DUMMY_CROSS_SELL.data.meta.uriWeb

        dataCrossSell.items.size shouldBe DUMMY_CROSS_SELL.data.items.size
        for ((index, item) in dataCrossSell.items.withIndex()) {
            item.product shouldBe DUMMY_CROSS_SELL.data.items[index].product
            item.value shouldBe DUMMY_CROSS_SELL.data.items[index].value
            item.uriWeb shouldBe DUMMY_CROSS_SELL.data.items[index].uriWeb
            item.title shouldBe DUMMY_CROSS_SELL.data.items[index].title
            item.prefix shouldBe DUMMY_CROSS_SELL.data.items[index].prefix
            item.imageUrl shouldBe DUMMY_CROSS_SELL.data.items[index].imageUrl
            item.content shouldBe DUMMY_CROSS_SELL.data.items[index].content
            item.uri shouldBe DUMMY_CROSS_SELL.data.items[index].uri
        }
    }

    @Test
    fun onNavigateToCancellationClicked() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA.journeys

        // when
        viewModel.onNavigateToCancellationClicked(dummyData)

        // then
        val cancellationData = viewModel.cancellationData.value!!
        for ((index, item) in cancellationData.withIndex()) {
            item.journeyId shouldBe dummyData[index].id.toString()
            item.departureTime shouldBe dummyData[index].departureTime
            item.departureCity shouldBe dummyData[index].departureCityName
            item.departureCityCode shouldBe dummyData[index].departureId
            item.departureAirportId shouldBe dummyData[index].departureAirportName
            item.arrivalTime shouldBe dummyData[index].arrivalTime
            item.arrivalCity shouldBe dummyData[index].arrivalCityName
            item.arrivalCityCode shouldBe dummyData[index].arrivalId
            item.arrivalAirportId shouldBe dummyData[index].arrivalAirportName
        }
    }

    @Test
    fun trackOpenOrderDetail() {
        // given
        coEvery { userSession.userId } returns "67890"
        viewModel.orderId = "12345"

        // when
        viewModel.trackOpenOrderDetail("berhasil")

        // then
        verify {
            flightAnalytics.openOrderDetail("berhasil - 12345", "67890")
        }
    }

    @Test
    fun isWebCheckInAvailable_whenOrderStatusNotSuccess_shouldNotAvailable() {
        // given
        val dummyData = DUMMY_FAILED_ORDER_DETAIL_DATA

        // when
        val isWebCheckInAvailable = viewModel.isWebCheckInAvailable(dummyData)

        // then
        isWebCheckInAvailable.first shouldBe false
        isWebCheckInAvailable.second shouldBe ""
    }

    @Test
    fun isWebCheckInAvailable_whenOrderStatusSuccessAndWebCheckInOpen_shouldBeAvailable() {
        // given
        // dummy data will be build on runtime because checkIn date need to be dynamic
        val dummyData = FlightOrderDetailDataModel(
                1,
                "0001-01-01T00:00:00Z",
                700,
                "Berhasil",
                1,
                "1234567890",
                "Muhammad Furqan",
                "email@email.com",
                "123456789012",
                "ID",
                "Rp1.000.000",
                1000000,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp1.000.000",
                1000000,
                "Rp",
                "dummy pdf",
                true,
                false,
                "Ekonomi",
                "dummy contact us URL",
                false,
                FlightOrderDetailPaymentModel(
                        1,
                        1,
                        "Paid",
                        "BCA Virtual Account",
                        "dummy icon",
                        "0001-01-01T00:00:00Z",
                        "",
                        "ididid",
                        "",
                        0,
                        "Rp0",
                        0,
                        "rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "BCA",
                        "Jakarta",
                        "1234567890",
                        "Furqan",
                        "Rp1.000.000"
                ),
                arrayListOf(
                        FlightOrderDetailJourneyModel(
                                1,
                                1,
                                "123",
                                "0001-01-01T00:00:00Z",
                                "departure airport",
                                "aceh",
                                "321",
                                "0001-01-01T00:00:00Z",
                                "arrival airport",
                                "jakarta",
                                0,
                                0,
                                0,
                                "2j",
                                120,
                                FlightOrderDetailFareModel(
                                        1000000,
                                        0,
                                        0
                                ),
                                arrayListOf(
                                        FlightOrderDetailRouteModel(
                                                "123",
                                                "departure time",
                                                "departure airport",
                                                "departure city",
                                                "321",
                                                "arrival time",
                                                "arrival airport",
                                                "arrival city",
                                                "123ASD",
                                                "890",
                                                "Seulawah Air",
                                                "",
                                                "",
                                                "AN-12345",
                                                "2j",
                                                120,
                                                "",
                                                0,
                                                true,
                                                "2",
                                                "",
                                                0,
                                                "",
                                                arrayListOf(),
                                                arrayListOf(),
                                                FlightOrderDetailFreeAmenityModel(
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        false,
                                                        false,
                                                        false,
                                                        arrayListOf()
                                                )
                                        )
                                ),
                                FlightOrderDetailWebCheckInModel(
                                        "Check In",
                                        "Check In Available",
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, -3), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, 3), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        "",
                                        "",
                                        ""
                                )
                        )
                ),
                arrayListOf(
                        FlightOrderDetailPassengerModel(
                                1,
                                1,
                                1,
                                "Dewasa",
                                1,
                                "Tuan",
                                "Muhammad",
                                "Furqan",
                                "",
                                "",
                                "",
                                "",
                                "",
                                arrayListOf(),
                                arrayListOf()
                        )
                ),
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
        )

        // when
        val isWebCheckInAvailable = viewModel.isWebCheckInAvailable(dummyData)

        // then
        isWebCheckInAvailable.first shouldBe true
        isWebCheckInAvailable.second shouldBe "Check In Available"
    }

    @Test
    fun isWebCheckInAvailable_whenOrderStatusSuccessButWebCheckInStartTimeNotOpenYet_shouldBeAvailable() {
        // given
        // dummy data will be build on runtime because checkIn date need to be dynamic
        val dummyData = FlightOrderDetailDataModel(
                1,
                "0001-01-01T00:00:00Z",
                700,
                "Berhasil",
                1,
                "1234567890",
                "Muhammad Furqan",
                "email@email.com",
                "123456789012",
                "ID",
                "Rp1.000.000",
                1000000,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp1.000.000",
                1000000,
                "Rp",
                "dummy pdf",
                true,
                false,
                "Ekonomi",
                "dummy contact us URL",
                false,
                FlightOrderDetailPaymentModel(
                        1,
                        1,
                        "Paid",
                        "BCA Virtual Account",
                        "dummy icon",
                        "0001-01-01T00:00:00Z",
                        "",
                        "ididid",
                        "",
                        0,
                        "Rp0",
                        0,
                        "rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "BCA",
                        "Jakarta",
                        "1234567890",
                        "Furqan",
                        "Rp1.000.000"
                ),
                arrayListOf(
                        FlightOrderDetailJourneyModel(
                                1,
                                1,
                                "123",
                                "0001-01-01T00:00:00Z",
                                "departure airport",
                                "aceh",
                                "321",
                                "0001-01-01T00:00:00Z",
                                "arrival airport",
                                "jakarta",
                                0,
                                0,
                                0,
                                "2j",
                                120,
                                FlightOrderDetailFareModel(
                                        1000000,
                                        0,
                                        0
                                ),
                                arrayListOf(
                                        FlightOrderDetailRouteModel(
                                                "123",
                                                "departure time",
                                                "departure airport",
                                                "departure city",
                                                "321",
                                                "arrival time",
                                                "arrival airport",
                                                "arrival city",
                                                "123ASD",
                                                "890",
                                                "Seulawah Air",
                                                "",
                                                "",
                                                "AN-12345",
                                                "2j",
                                                120,
                                                "",
                                                0,
                                                true,
                                                "2",
                                                "",
                                                0,
                                                "",
                                                arrayListOf(),
                                                arrayListOf(),
                                                FlightOrderDetailFreeAmenityModel(
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        false,
                                                        false,
                                                        false,
                                                        arrayListOf()
                                                )
                                        )
                                ),
                                FlightOrderDetailWebCheckInModel(
                                        "Check In",
                                        "Check In Not Available",
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, 1), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, 2), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        "",
                                        "",
                                        ""
                                )
                        )
                ),
                arrayListOf(
                        FlightOrderDetailPassengerModel(
                                1,
                                1,
                                1,
                                "Dewasa",
                                1,
                                "Tuan",
                                "Muhammad",
                                "Furqan",
                                "",
                                "",
                                "",
                                "",
                                "",
                                arrayListOf(),
                                arrayListOf()
                        )
                ),
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
        )

        // when
        val isWebCheckInAvailable = viewModel.isWebCheckInAvailable(dummyData)

        // then
        isWebCheckInAvailable.first shouldBe false
        isWebCheckInAvailable.second shouldBe "Check In Not Available"
    }

    @Test
    fun isWebCheckInAvailable_whenOrderStatusSuccessButWebCheckInStartTimeOpenButEndTimePassed_shouldBeAvailable() {
        // given
        // dummy data will be build on runtime because checkIn date need to be dynamic
        val dummyData = FlightOrderDetailDataModel(
                1,
                "0001-01-01T00:00:00Z",
                700,
                "Berhasil",
                1,
                "1234567890",
                "Muhammad Furqan",
                "email@email.com",
                "123456789012",
                "ID",
                "Rp1.000.000",
                1000000,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp1.000.000",
                1000000,
                "Rp",
                "dummy pdf",
                true,
                false,
                "Ekonomi",
                "dummy contact us URL",
                false,
                FlightOrderDetailPaymentModel(
                        1,
                        1,
                        "Paid",
                        "BCA Virtual Account",
                        "dummy icon",
                        "0001-01-01T00:00:00Z",
                        "",
                        "ididid",
                        "",
                        0,
                        "Rp0",
                        0,
                        "rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "BCA",
                        "Jakarta",
                        "1234567890",
                        "Furqan",
                        "Rp1.000.000"
                ),
                arrayListOf(
                        FlightOrderDetailJourneyModel(
                                1,
                                1,
                                "123",
                                "0001-01-01T00:00:00Z",
                                "departure airport",
                                "aceh",
                                "321",
                                "0001-01-01T00:00:00Z",
                                "arrival airport",
                                "jakarta",
                                0,
                                0,
                                0,
                                "2j",
                                120,
                                FlightOrderDetailFareModel(
                                        1000000,
                                        0,
                                        0
                                ),
                                arrayListOf(
                                        FlightOrderDetailRouteModel(
                                                "123",
                                                "departure time",
                                                "departure airport",
                                                "departure city",
                                                "321",
                                                "arrival time",
                                                "arrival airport",
                                                "arrival city",
                                                "123ASD",
                                                "890",
                                                "Seulawah Air",
                                                "",
                                                "",
                                                "AN-12345",
                                                "2j",
                                                120,
                                                "",
                                                0,
                                                true,
                                                "2",
                                                "",
                                                0,
                                                "",
                                                arrayListOf(),
                                                arrayListOf(),
                                                FlightOrderDetailFreeAmenityModel(
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        false,
                                                        false,
                                                        false,
                                                        arrayListOf()
                                                )
                                        )
                                ),
                                FlightOrderDetailWebCheckInModel(
                                        "Check In",
                                        "Check In Not Available",
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, 1), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, -2), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        "",
                                        "",
                                        ""
                                )
                        )
                ),
                arrayListOf(
                        FlightOrderDetailPassengerModel(
                                1,
                                1,
                                1,
                                "Dewasa",
                                1,
                                "Tuan",
                                "Muhammad",
                                "Furqan",
                                "",
                                "",
                                "",
                                "",
                                "",
                                arrayListOf(),
                                arrayListOf()
                        )
                ),
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
        )

        // when
        val isWebCheckInAvailable = viewModel.isWebCheckInAvailable(dummyData)

        // then
        isWebCheckInAvailable.first shouldBe false
        isWebCheckInAvailable.second shouldBe "Check In Not Available"
    }

    @Test
    fun isWebCheckInAvailable_whenOrderStatusSuccessButWebCheckInStartAndEndTimePassed_shouldBeAvailable() {
        // given
        // dummy data will be build on runtime because checkIn date need to be dynamic
        val dummyData = FlightOrderDetailDataModel(
                1,
                "0001-01-01T00:00:00Z",
                700,
                "Berhasil",
                1,
                "1234567890",
                "Muhammad Furqan",
                "email@email.com",
                "123456789012",
                "ID",
                "Rp1.000.000",
                1000000,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp1.000.000",
                1000000,
                "Rp",
                "dummy pdf",
                true,
                false,
                "Ekonomi",
                "dummy contact us URL",
                false,
                FlightOrderDetailPaymentModel(
                        1,
                        1,
                        "Paid",
                        "BCA Virtual Account",
                        "dummy icon",
                        "0001-01-01T00:00:00Z",
                        "",
                        "ididid",
                        "",
                        0,
                        "Rp0",
                        0,
                        "rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "Rp0",
                        0,
                        "BCA",
                        "Jakarta",
                        "1234567890",
                        "Furqan",
                        "Rp1.000.000"
                ),
                arrayListOf(
                        FlightOrderDetailJourneyModel(
                                1,
                                1,
                                "123",
                                "0001-01-01T00:00:00Z",
                                "departure airport",
                                "aceh",
                                "321",
                                "0001-01-01T00:00:00Z",
                                "arrival airport",
                                "jakarta",
                                0,
                                0,
                                0,
                                "2j",
                                120,
                                FlightOrderDetailFareModel(
                                        1000000,
                                        0,
                                        0
                                ),
                                arrayListOf(
                                        FlightOrderDetailRouteModel(
                                                "123",
                                                "departure time",
                                                "departure airport",
                                                "departure city",
                                                "321",
                                                "arrival time",
                                                "arrival airport",
                                                "arrival city",
                                                "123ASD",
                                                "890",
                                                "Seulawah Air",
                                                "",
                                                "",
                                                "AN-12345",
                                                "2j",
                                                120,
                                                "",
                                                0,
                                                true,
                                                "2",
                                                "",
                                                0,
                                                "",
                                                arrayListOf(),
                                                arrayListOf(),
                                                FlightOrderDetailFreeAmenityModel(
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                        false,
                                                        false,
                                                        false,
                                                        arrayListOf()
                                                )
                                        )
                                ),
                                FlightOrderDetailWebCheckInModel(
                                        "Check In",
                                        "Check In Not Available",
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, -2), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        FlightDateUtil.dateToString(FlightDateUtil.addTimeToCurrentDate(Calendar.HOUR, -1), FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z),
                                        "",
                                        "",
                                        ""
                                )
                        )
                ),
                arrayListOf(
                        FlightOrderDetailPassengerModel(
                                1,
                                1,
                                1,
                                "Dewasa",
                                1,
                                "Tuan",
                                "Muhammad",
                                "Furqan",
                                "",
                                "",
                                "",
                                "",
                                "",
                                arrayListOf(),
                                arrayListOf()
                        )
                ),
                arrayListOf(),
                arrayListOf(),
                arrayListOf(),
                arrayListOf()
        )

        // when
        val isWebCheckInAvailable = viewModel.isWebCheckInAvailable(dummyData)

        // then
        isWebCheckInAvailable.first shouldBe false
        isWebCheckInAvailable.second shouldBe "Check In Not Available"
    }

    @Test
    fun buildPaymentDetailData_failedToFetchOrderDetail() {
        // given
        coEvery { useCase.execute(any(), any()) } coAnswers { throw Throwable() }

        // when
        val paymentData = viewModel.buildPaymentDetailData()

        // then
        paymentData.size shouldBe 0
    }

    @Test
    fun buildPaymentDetailData_successToFetchOrderDetail() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        viewModel.fetchOrderDetailData()

        // when
        val paymentData = viewModel.buildPaymentDetailData()

        // then
        paymentData.size shouldBe 3
        // check adult passenger paymentData
        paymentData[0].leftValue shouldBe "CGK - BTJ Dewasa x1"
        paymentData[0].rightValue shouldBe "Rp1.000.000"
        paymentData[0].isLeftBold shouldBe false
        paymentData[0].isRightBold shouldBe false
        paymentData[0].isLeftStriked shouldBe false
        paymentData[0].isRightStriked shouldBe false
        paymentData[0].isRightAlign shouldBe true
        // check child passenger paymentData
        paymentData[1].leftValue shouldBe "CGK - BTJ Anak x1"
        paymentData[1].rightValue shouldBe "Rp100"
        paymentData[1].isLeftBold shouldBe false
        paymentData[1].isRightBold shouldBe false
        paymentData[1].isLeftStriked shouldBe false
        paymentData[1].isRightStriked shouldBe false
        paymentData[1].isRightAlign shouldBe true
        // check infant passenger paymentData
        paymentData[2].leftValue shouldBe "CGK - BTJ Bayi x1"
        paymentData[2].rightValue shouldBe "Rp10"
        paymentData[2].isLeftBold shouldBe false
        paymentData[2].isRightBold shouldBe false
        paymentData[2].isLeftStriked shouldBe false
        paymentData[2].isRightStriked shouldBe false
        paymentData[2].isRightAlign shouldBe true
    }

    @Test
    fun buildAmenitiesPaymentDetailData_failedToFetchOrderDetail() {
        // given
        coEvery { useCase.execute(any(), any()) } coAnswers { throw Throwable() }

        // when
        val paymentData = viewModel.buildAmenitiesPaymentDetailData()

        // then
        paymentData.size shouldBe 0
    }

    @Test
    fun buildAmenitiesPaymentDetailData_successToFetchOrderDetail() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        viewModel.fetchOrderDetailData()

        // when
        val paymentData = viewModel.buildAmenitiesPaymentDetailData()

        // then
        paymentData.size shouldBe 3
        // check luggage paymentData
        paymentData[0].leftValue shouldBe "Bagasi CGK - BTJ"
        paymentData[0].rightValue shouldBe "Rp1.000"
        paymentData[0].isLeftBold shouldBe false
        paymentData[0].isRightBold shouldBe false
        paymentData[0].isLeftStriked shouldBe false
        paymentData[0].isRightStriked shouldBe false
        paymentData[0].isRightAlign shouldBe true
        // check meal paymentData
        paymentData[1].leftValue shouldBe "Makanan CGK - BTJ"
        paymentData[1].rightValue shouldBe "Rp1.000"
        paymentData[1].isLeftBold shouldBe false
        paymentData[1].isRightBold shouldBe false
        paymentData[1].isLeftStriked shouldBe false
        paymentData[1].isRightStriked shouldBe false
        paymentData[1].isRightAlign shouldBe true
        // check wrong paymentData
        paymentData[2].leftValue shouldBe ""
        paymentData[2].rightValue shouldBe "Rp1.000"
        paymentData[2].isLeftBold shouldBe false
        paymentData[2].isRightBold shouldBe false
        paymentData[2].isLeftStriked shouldBe false
        paymentData[2].isRightStriked shouldBe false
        paymentData[2].isRightAlign shouldBe true
    }

    @Test
    fun getTotalAmount() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        viewModel.fetchOrderDetailData()

        // when
        val totalAmount = viewModel.getTotalAmount()

        // then
        totalAmount shouldBe "Rp1.000.000"
    }

    @Test
    fun getOrderDetailStatus_withNullOrderDetail() {
        // given

        // when
        val orderDetailStatus = viewModel.getOrderDetailStatus()

        // then
        orderDetailStatus shouldBe ""
    }

    @Test
    fun getOrderDetailStatus_withOrderDetailData() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        viewModel.fetchOrderDetailData()

        // when
        val orderDetailStatus = viewModel.getOrderDetailStatus()

        // then
        orderDetailStatus shouldBe "Berhasil"
    }

    @Test
    fun trackSendETicketClicked() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        coEvery { userSession.userId } returns "0987654321"
        viewModel.orderId = "1234567890"
        viewModel.fetchOrderDetailData()

        // when
        viewModel.trackSendETicketClicked()

        // then
        coVerify {
            flightAnalytics.eventSendETicketOrderDetail(
                    "Berhasil - 1234567890",
                    "0987654321"
            )
        }
    }

    @Test
    fun trackClickWebCheckIn() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        coEvery { userSession.userId } returns "0987654321"
        viewModel.orderId = "1234567890"
        viewModel.fetchOrderDetailData()

        // when
        viewModel.trackClickWebCheckIn()

        // then
        coVerify {
            flightAnalytics.eventWebCheckInOrderDetail(
                    "Berhasil - 1234567890",
                    "0987654321"
            )
        }
    }

    @Test
    fun trackClickCancel() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { useCase.execute(any(), any()) } returns dummyData
        coEvery { userSession.userId } returns "0987654321"
        viewModel.orderId = "1234567890"
        viewModel.fetchOrderDetailData()

        // when
        viewModel.trackClickCancel()

        // then
        coVerify {
            flightAnalytics.eventCancelTicketOrderDetail(
                    "Berhasil - 1234567890",
                    "0987654321"
            )
        }
    }

}