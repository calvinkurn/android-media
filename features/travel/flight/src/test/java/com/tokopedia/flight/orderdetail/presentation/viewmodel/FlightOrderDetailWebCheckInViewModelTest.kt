package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.dummy.DUMMY_ORDER_DETAIL_DATA
import com.tokopedia.flight.dummy.DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE
import com.tokopedia.flight.dummy.DUMMY_ORDER_DETAIL_JOURNEY_MULTI_AIRLINE
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.*
import com.tokopedia.flight.shouldBe
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
 * @author by furqan on 13/12/2020
 */
class FlightOrderDetailWebCheckInViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var flightAnalytics: FlightAnalytics

    private val orderDetailUseCase: FlightOrderDetailUseCase = mockk()
    private val userSession: UserSessionInterface = mockk()
    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: FlightOrderDetailWebCheckInViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = FlightOrderDetailWebCheckInViewModel(
                orderDetailUseCase,
                flightAnalytics,
                userSession,
                dispatcherProvider
        )
    }


    @Test
    fun fetchOrderDetailData_errorFetching_shouldReturnFail() {
        // given
        coEvery { orderDetailUseCase.execute(any(), any()) } coAnswers { throw MessageErrorException("Error Fetch Data") }
        viewModel.orderId = "1234567890"

        // when
        viewModel.fetchOrderDetailData()

        // then
        assert(viewModel.orderDetailData.value is Fail)
        (viewModel.orderDetailData.value as Fail).throwable.message shouldBe "Error Fetch Data"
    }

    @Test
    fun fetchOrderDetailData_successFetchingSingleAirline_shouldReturnSuccessWithData() {
        // given
        coEvery { orderDetailUseCase.execute(any(), any()) } returns DUMMY_ORDER_DETAIL_DATA
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
        coEvery { orderDetailUseCase.execute(any(), any()) } returns DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE
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
    fun trackOnCheckInDeparture_MultiAirline() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { orderDetailUseCase.execute(any(), any()) } returns dummyData
        coEvery { userSession.userId } returns "0987654321"
        viewModel.orderId = "1234567890"
        viewModel.fetchOrderDetailData()

        // when
        viewModel.trackOnCheckInDeparture(DUMMY_ORDER_DETAIL_JOURNEY_MULTI_AIRLINE, false)

        // then
        coVerify {
            flightAnalytics.eventClickOnWebCheckIn(
                    "123321 - Seulawah Air + Garuda Indonesia - 00010101 - 1234567890",
                    "0987654321",
                    false
            )
        }
    }

    @Test
    fun trackOnCheckInDeparture_SingleAirline() {
        // given
        val dummyData = DUMMY_ORDER_DETAIL_DATA
        coEvery { orderDetailUseCase.execute(any(), any()) } returns dummyData
        coEvery { userSession.userId } returns "0987654321"
        viewModel.orderId = "1234567890"
        viewModel.fetchOrderDetailData()

        // when
        viewModel.trackOnCheckInDeparture(FlightOrderDetailJourneyModel(
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
                                "logo seulawah air",
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
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        ), false)

        // then
        coVerify {
            flightAnalytics.eventClickOnWebCheckIn(
                    any(),
                    "0987654321",
                    false
            )
        }
    }

}