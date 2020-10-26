package com.tokopedia.flight.orderdetail.domain

import com.tokopedia.flight.orderdetail.data.FlightOrderDetailEntity
import com.tokopedia.flight.orderdetail.data.FlightOrderDetailGqlConst
import com.tokopedia.flight.orderdetail.presentation.model.*
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * @author by furqan on 16/10/2020
 */
class FlightOrderDetailUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(invoiceId: String, isFromCloud: Boolean = true): OrderDetailDataModel {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(
                if (isFromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_DATA to mapOf(PARAM_INVOICE_ID to invoiceId))
        val graphqlRequest = GraphqlRequest(FlightOrderDetailGqlConst.QUERY_ORDER_DETAIL,
                FlightOrderDetailEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val orderDetailEntity = useCase.executeOnBackground().getSuccessData<FlightOrderDetailEntity.Response>().flightGetOrderDetail

        if (orderDetailEntity.errors.isEmpty()) {
            return transformEntityToModel(orderDetailEntity.data)
        } else {
            throw MessageErrorException(orderDetailEntity.errors.joinToString(separator = ";") { it.message })
        }
    }

    private fun transformEntityToModel(orderDetailData: FlightOrderDetailEntity.OrderDetailData): OrderDetailDataModel =
            orderDetailData.let {
                OrderDetailDataModel(
                        omsId = it.omsId,
                        createTime = it.createTime,
                        status = it.status,
                        statusString = it.statusString,
                        id = it.flight.id,
                        invoiceId = it.flight.invoiceId,
                        contactName = it.flight.contactName,
                        email = it.flight.email,
                        phone = it.flight.phone,
                        countryId = it.flight.countryId,
                        totalAdult = it.flight.totalAdult,
                        totalAdultNumeric = it.flight.totalAdultNumeric,
                        totalChild = it.flight.totalChild,
                        totalChildNumeric = it.flight.totalChildNumeric,
                        totalInfant = it.flight.totalInfant,
                        totalInfantNumeric = it.flight.totalInfantNumeric,
                        totalPrice = it.flight.totalPrice,
                        totalPriceNumeric = it.flight.totalPriceNumeric,
                        currency = it.flight.currency,
                        pdf = it.flight.pdf,
                        isDomestic = it.flight.isDomestic,
                        mandatoryDob = it.flight.mandatoryDob,
                        classText = it.flight.classText,
                        contactUsURL = it.flight.contactUsURL,
                        payment = it.flight.payment.let { payment ->
                            OrderDetailPaymentModel(
                                    id = payment.id,
                                    status = payment.status,
                                    statusStr = payment.statusStr,
                                    gatewayName = payment.gatewayName,
                                    gatewayIcon = payment.gatewayIcon,
                                    paymentDate = payment.paymentDate,
                                    expireOn = payment.expireOn,
                                    transactionCode = payment.transactionCode,
                                    promoCode = payment.promoCode,
                                    adminFeeAmount = payment.adminFeeAmount,
                                    adminFeeAmountStr = payment.adminFeeAmountStr,
                                    voucherAmount = payment.voucherAmount,
                                    voucherAmountStr = payment.voucherAmountStr,
                                    saldoAmount = payment.saldoAmount,
                                    saldoAmountStr = payment.saldoAmountStr,
                                    totalAmount = payment.totalAmount,
                                    totalAmountStr = payment.totalAmountString,
                                    needToPayAmount = payment.needToPayAmount,
                                    needToPayAmountStr = payment.needToPayAmountStr,
                                    uniqueCode = payment.manualTransfer.uniqueCode,
                                    accountBankName = payment.manualTransfer.accountBankName,
                                    accountBranch = payment.manualTransfer.accountBranch,
                                    accountNo = payment.manualTransfer.accountNo,
                                    accountName = payment.manualTransfer.accountName,
                                    total = payment.manualTransfer.total
                            )
                        },
                        journeys = it.flight.journeys.map { journey ->
                            OrderDetailJourneyModel(
                                    id = journey.id,
                                    status = journey.status,
                                    departureId = journey.departureId,
                                    departureTime = journey.departureTime,
                                    departureAirportName = journey.departureAirportName,
                                    departureCityName = journey.departureCityName,
                                    arrivalId = journey.arrivalId,
                                    arrivalTime = journey.arrivalTime,
                                    arrivalAirportName = journey.arrivalAirportName,
                                    arrivalCityName = journey.arrivalCityName,
                                    totalTransit = journey.totalTransit,
                                    totalStop = journey.totalStop,
                                    addDayArrival = journey.addDayArrival,
                                    duration = journey.duration,
                                    durationMinute = journey.durationMinute,
                                    fare = OrderDetailFareModel(
                                            adultNumeric = journey.fare.adultNumeric,
                                            childNumeric = journey.fare.childNumeric,
                                            infantNumeric = journey.fare.infantNumeric
                                    ),
                                    routes = journey.routes.map { route ->
                                        OrderDetailRouteModel(
                                                departureId = route.departureId,
                                                departureTime = route.departureTime,
                                                departureAirportName = route.departureAirportName,
                                                departureCityName = route.departureCityName,
                                                arrivalId = route.arrivalId,
                                                arrivalTime = route.arrivalTime,
                                                arrivalAirportName = route.arrivalAirportName,
                                                arrivalCityName = route.arrivalCityName,
                                                pnr = route.pnr,
                                                airlineId = route.airlineId,
                                                airlineName = route.airlineName,
                                                airlineLogo = route.airlineLogo,
                                                operatorAirlineId = route.operatorAirlineId,
                                                flightNumber = route.flightNumber,
                                                duration = route.duration,
                                                durationMinute = route.durationMinute,
                                                layover = route.layover,
                                                layoverMinute = route.layoverMinute,
                                                refundable = route.refundable,
                                                departureTerminal = route.departureTerminal,
                                                arrivalTerminal = route.arrivalTerminal,
                                                stop = route.stop,
                                                carrier = route.carrier,
                                                stopDetails = route.stopDetails,
                                                ticketNumbers = route.ticketNumbers.map { ticket ->
                                                    OrderDetailRouteModel.OrderDetailTicketNumberModel(
                                                            passengerId = ticket.passengerId,
                                                            ticketNumber = ticket.ticketNumber
                                                    )
                                                }.toList(),
                                                freeAmenities = OrderDetailFreeAmenityModel(
                                                        cabinBaggage = OrderDetailFreeAmenityModel.OrderDetailBaggageModel(
                                                                isUpTo = route.freeAmenities.cabinBaggage.isUpTo,
                                                                unit = route.freeAmenities.cabinBaggage.unit,
                                                                value = route.freeAmenities.cabinBaggage.value
                                                        ),
                                                        freeBaggage = OrderDetailFreeAmenityModel.OrderDetailBaggageModel(
                                                                isUpTo = route.freeAmenities.freeBaggage.isUpTo,
                                                                unit = route.freeAmenities.freeBaggage.unit,
                                                                value = route.freeAmenities.freeBaggage.value
                                                        ),
                                                        meal = route.freeAmenities.meal,
                                                        usbPort = route.freeAmenities.usbPort,
                                                        wifi = route.freeAmenities.wifi,
                                                        others = route.freeAmenities.others
                                                )
                                        )
                                    }.toList(),
                                    webCheckIn = OrderDetailWebCheckInModel(
                                            title = journey.webCheckIn.title,
                                            subtitle = journey.webCheckIn.subtitle,
                                            startTime = journey.webCheckIn.startTime,
                                            endTime = journey.webCheckIn.endTime,
                                            iconUrl = journey.webCheckIn.iconUrl,
                                            appUrl = journey.webCheckIn.appUrl,
                                            webUrl = journey.webCheckIn.webUrl
                                    )
                            )
                        }.toList(),
                        passengers = it.flight.passengers.map { passenger ->
                            OrderDetailPassengerModel(
                                    id = passenger.id,
                                    type = passenger.type,
                                    title = passenger.title,
                                    firstName = passenger.firstName,
                                    lastName = passenger.lastName,
                                    dob = passenger.dob,
                                    nationality = passenger.nationality,
                                    passportNo = passenger.passportNo,
                                    passportCountry = passenger.passportCountry,
                                    passportExpiry = passenger.passportExpiry,
                                    amenities = passenger.amenities.map { amenity ->
                                        OrderDetailAmenityModel(
                                                departureId = amenity.departureId,
                                                arrivalId = amenity.arrivalId,
                                                type = amenity.type,
                                                price = amenity.price,
                                                priceNumeric = amenity.priceNumeric,
                                                detail = amenity.detail
                                        )
                                    }.toList(),
                                    cancelStatus = passenger.cancelStatus.map { cancel ->
                                        OrderDetailPassengerCancelStatusModel(
                                                status = cancel.status,
                                                statusStr = cancel.statusStr,
                                                statusType = cancel.statusType
                                        )
                                    }.toList()
                            )
                        }.toList(),
                        actionButtons = it.flight.actionButtons.map { actionButton ->
                            OrderDetailActionButtonModel(
                                    id = actionButton.id,
                                    label = actionButton.label,
                                    buttonType = actionButton.buttonType,
                                    uri = actionButton.uri,
                                    uriWeb = actionButton.uriWeb,
                                    mappingUrl = actionButton.mappingUrl,
                                    method = actionButton.method,
                                    weight = actionButton.weight
                            )
                        }.toList(),
                        conditionalInfos = it.flight.conditionalInfo.map { conditionalInfo ->
                            OrderDetailConditionalInfoModel(
                                    type = conditionalInfo.type,
                                    title = conditionalInfo.title,
                                    text = conditionalInfo.text,
                                    border = conditionalInfo.color.border,
                                    background = conditionalInfo.color.background
                            )
                        }.toList(),
                        insurances = it.flight.insurances.map { insurance ->
                            OrderDetailInsuranceModel(
                                    id = insurance.id,
                                    title = insurance.title,
                                    tagline = insurance.tagline,
                                    paidAmount = insurance.paidAmount,
                                    paidAmountNumeric = insurance.paidAmountNumeric
                            )
                        }.toList(),
                        cancellations = it.flight.cancellations.map { cancellation ->
                            OrderDetailCancellationModel(
                                    cancelId = cancellation.cancelId,
                                    journeyId = cancellation.cancelDetail.journeyId,
                                    passengerId = cancellation.cancelDetail.passengerId,
                                    refundedGateway = cancellation.cancelDetail.refundedGateway,
                                    refundedTime = cancellation.cancelDetail.refundedTime,
                                    createTime = cancellation.createTime,
                                    updateTime = cancellation.updateTime,
                                    estimatedRefund = cancellation.estimatedRefund,
                                    estimatedRefundNumeric = cancellation.estimatedRefundNumeric,
                                    realRefund = cancellation.realRefund,
                                    realRefundNumeric = cancellation.realRefundNumeric,
                                    status = cancellation.status,
                                    statusStr = cancellation.statusStr,
                                    statusType = cancellation.statusType,
                                    refundInfo = cancellation.refundInfo,
                                    refundDetail = OrderDetailCancellationModel.OrderDetailRefundDetailModel(
                                            topInfo = cancellation.refundDetail.topInfo.map { topInfo ->
                                                OrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                        key = topInfo.key,
                                                        value = topInfo.value
                                                )
                                            }.toList(),
                                            middleInfo = cancellation.refundDetail.middleInfo.map { middleInfo ->
                                                OrderDetailCancellationModel.OrderDetailRefundTitleContentModel(
                                                        title = middleInfo.title,
                                                        content = middleInfo.content.map { content ->
                                                            OrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                                    key = content.key,
                                                                    value = content.value
                                                            )
                                                        }.toList()
                                                )
                                            }.toList(),
                                            bottomInfo = cancellation.refundDetail.bottomInfo.map { bottomInfo ->
                                                OrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                        key = bottomInfo.key,
                                                        value = bottomInfo.value
                                                )
                                            }.toList(),
                                            notes = cancellation.refundDetail.notes
                                    )
                            )
                        }.toList()
                )
            }


    companion object {
        private const val PARAM_INVOICE_ID = "invoiceID"
        private const val PARAM_DATA = "data"
    }

}