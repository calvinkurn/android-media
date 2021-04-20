package com.tokopedia.flight.orderdetail.domain

import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.enum.FlightPassengerTitle
import com.tokopedia.flight.common.view.enum.FlightPassengerType
import com.tokopedia.flight.orderdetail.data.*
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

    suspend fun execute(invoiceId: String, isFromCloud: Boolean = true): FlightOrderDetailDataModel {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(
                if (isFromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_DATA to mapOf(PARAM_INVOICE_ID to invoiceId))
        val graphqlRequest = GraphqlRequest(FlightOrderDetailGqlConst.QUERY_ORDER_DETAIL,
                FlightOrderDetailEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val graphqlResponse = useCase.executeOnBackground()
        val orderDetailResponse = graphqlResponse.getSuccessData<FlightOrderDetailEntity.Response>()
        val orderDetailEntity = orderDetailResponse.flightGetOrderDetail

        if (orderDetailEntity.errors.isEmpty()) {
            return transformEntityToModel(orderDetailEntity.data)
        } else {
            throw MessageErrorException(orderDetailEntity.errors.joinToString(separator = ";") { it.message })
        }
    }

    suspend fun executeGetCancellationList(invoiceId: String): List<FlightOrderCancellationListModel> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_DATA to mapOf(PARAM_INVOICE_ID to invoiceId))
        val graphqlRequest = GraphqlRequest(FlightOrderDetailGqlConst.QUERY_ORDER_DETAIL,
                FlightOrderDetailEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val graphqlResponse = useCase.executeOnBackground()
        val orderDetailResponse = graphqlResponse.getSuccessData<FlightOrderDetailEntity.Response>()
        val orderDetailEntity = orderDetailResponse.flightGetOrderDetail

        if (orderDetailEntity.errors.isEmpty()) {
            return transformOrderDetailToCancellationList(orderDetailEntity.data)
        } else {
            throw MessageErrorException(orderDetailEntity.errors.joinToString(separator = ";") { it.message })
        }
    }

    private fun transformEntityToModel(orderDetailData: FlightOrderDetailEntity.OrderDetailData): FlightOrderDetailDataModel =
            orderDetailData.let {
                FlightOrderDetailDataModel(
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
                        hasETicket = it.flight.hasETicket,
                        payment = it.flight.payment.let { payment ->
                            FlightOrderDetailPaymentModel(
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
                            transformOrderDetailJourney(journey)
                        }.toList(),
                        passengers = it.flight.passengers.mapIndexed { index, passenger ->
                            FlightOrderDetailPassengerModel(
                                    passengerNo = index + 1,
                                    id = passenger.id,
                                    type = passenger.type,
                                    typeString = getPassengerTypeString(passenger.type),
                                    title = passenger.title,
                                    titleString = getPassengerTitleString(passenger.title),
                                    firstName = passenger.firstName,
                                    lastName = passenger.lastName,
                                    dob = passenger.dob,
                                    nationality = passenger.nationality,
                                    passportNo = passenger.passportNo,
                                    passportCountry = passenger.passportCountry,
                                    passportExpiry = passenger.passportExpiry,
                                    amenities = passenger.amenities.map { amenity ->
                                        FlightOrderDetailAmenityModel(
                                                departureId = amenity.departureId,
                                                arrivalId = amenity.arrivalId,
                                                type = amenity.type,
                                                price = amenity.price,
                                                priceNumeric = amenity.priceNumeric,
                                                detail = amenity.detail
                                        )
                                    }.toList(),
                                    cancelStatus = passenger.cancelStatus.map { cancel ->
                                        FlightOrderDetailPassengerCancelStatusModel(
                                                status = cancel.status,
                                                statusStr = cancel.statusStr,
                                                statusType = cancel.statusType
                                        )
                                    }.toList()
                            )
                        }.toList(),
                        actionButtons = it.flight.actionButtons.map { actionButton ->
                            FlightOrderDetailActionButtonModel(
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
                            FlightOrderDetailConditionalInfoModel(
                                    type = conditionalInfo.type,
                                    title = conditionalInfo.title,
                                    text = conditionalInfo.text,
                                    border = conditionalInfo.color.border,
                                    background = conditionalInfo.color.background
                            )
                        }.toList(),
                        insurances = it.flight.insurances.map { insurance ->
                            FlightOrderDetailInsuranceModel(
                                    id = insurance.id,
                                    title = insurance.title,
                                    tagline = insurance.tagline,
                                    paidAmount = insurance.paidAmount,
                                    paidAmountNumeric = insurance.paidAmountNumeric
                            )
                        }.toList(),
                        cancellations = it.flight.cancellations.map { cancellation ->
                            FlightOrderDetailCancellationModel(
                                    cancelId = cancellation.cancelId,
                                    cancelDetails = cancellation.cancelDetail.map { cancelDetail ->
                                        FlightOrderDetailCancellationModel.OrderDetailCancellationDetail(
                                                journeyId = cancelDetail.journeyId,
                                                passengerId = cancelDetail.passengerId,
                                                refundedGateway = cancelDetail.refundedGateway,
                                                refundedTime = cancelDetail.refundedTime
                                        )
                                    }.toList(),
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
                                    refundDetail = FlightOrderDetailCancellationModel.OrderDetailRefundDetailModel(
                                            topInfo = cancellation.refundDetail.topInfo.map { topInfo ->
                                                FlightOrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                        key = topInfo.key,
                                                        value = topInfo.value
                                                )
                                            }.toList(),
                                            middleInfo = cancellation.refundDetail.middleInfo.map { middleInfo ->
                                                FlightOrderDetailCancellationModel.OrderDetailRefundTitleContentModel(
                                                        title = middleInfo.title,
                                                        content = middleInfo.content.map { content ->
                                                            FlightOrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                                    key = content.key,
                                                                    value = content.value
                                                            )
                                                        }.toList()
                                                )
                                            }.toList(),
                                            bottomInfo = cancellation.refundDetail.bottomInfo.map { bottomInfo ->
                                                FlightOrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                        key = bottomInfo.key,
                                                        value = bottomInfo.value
                                                )
                                            }.toList(),
                                            notes = cancellation.refundDetail.notes.map { notes ->
                                                FlightOrderDetailCancellationModel.OrderDetailRefundKeyValueModel(
                                                        notes.key,
                                                        notes.value
                                                )
                                            }
                                    )
                            )
                        }.toList()
                )
            }

    private fun transformOrderDetailJourney(journey: OrderDetailJourney): FlightOrderDetailJourneyModel =
            FlightOrderDetailJourneyModel(
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
                    fare = FlightOrderDetailFareModel(
                            adultNumeric = journey.fare.adultNumeric,
                            childNumeric = journey.fare.childNumeric,
                            infantNumeric = journey.fare.infantNumeric
                    ),
                    routes = journey.routes.map { route ->
                        FlightOrderDetailRouteModel(
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
                                    FlightOrderDetailRouteModel.OrderDetailTicketNumberModel(
                                            passengerId = ticket.passengerId,
                                            ticketNumber = ticket.ticketNumber
                                    )
                                }.toList(),
                                freeAmenities = FlightOrderDetailFreeAmenityModel(
                                        cabinBaggage = FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(
                                                isUpTo = route.freeAmenities.cabinBaggage.isUpTo,
                                                unit = route.freeAmenities.cabinBaggage.unit,
                                                value = route.freeAmenities.cabinBaggage.value
                                        ),
                                        freeBaggage = FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(
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
                    webCheckIn = FlightOrderDetailWebCheckInModel(
                            title = journey.webCheckIn.title,
                            subtitle = journey.webCheckIn.subtitle,
                            startTime = journey.webCheckIn.startTime,
                            endTime = journey.webCheckIn.endTime,
                            iconUrl = journey.webCheckIn.iconUrl,
                            appUrl = journey.webCheckIn.appUrl,
                            webUrl = journey.webCheckIn.webUrl
                    )
            )

    private fun getPassengerTitleString(titleId: Int): String =
            when (titleId) {
                FlightPassengerTitle.TUAN.id -> FlightPassengerTitle.TUAN.salutation
                FlightPassengerTitle.NYONYA.id -> FlightPassengerTitle.NYONYA.salutation
                FlightPassengerTitle.NONA.id -> FlightPassengerTitle.NONA.salutation
                else -> FlightPassengerTitle.TUAN.salutation
            }

    private fun getPassengerTypeString(typeId: Int): String =
            when (typeId) {
                FlightPassengerType.ADULT.id -> FlightPassengerType.ADULT.type
                FlightPassengerType.CHILDREN.id -> FlightPassengerType.CHILDREN.type
                FlightPassengerType.INFANT.id -> FlightPassengerType.INFANT.type
                else -> FlightPassengerType.ADULT.type
            }

    private fun transformOrderDetailToCancellationList(data: FlightOrderDetailEntity.OrderDetailData): List<FlightOrderCancellationListModel> =
            data.let {
                val cancellationList = arrayListOf<FlightOrderCancellationListModel>()
                val journeyIdMapToDepartureArrivalId = hashMapOf<Int, Pair<String, String>>()

                data.flight.journeys.map { journey ->
                    journeyIdMapToDepartureArrivalId.put(journey.id, Pair(journey.departureId, journey.arrivalId))
                }

                for (item in it.flight.cancellations) {
                    cancellationList.add(FlightOrderCancellationListModel(
                            orderId = it.flight.invoiceId,
                            cancellationDetail = FlightOrderCancellationDetailModel(
                                    refundId = item.cancelId,
                                    createTime = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                                            FlightDateUtil.DEFAULT_VIEW_TIME_FORMAT, item.createTime),
                                    realRefund = item.realRefund,
                                    status = item.status,
                                    statusStr = item.statusStr,
                                    statusType = item.statusType,
                                    refundInfo = item.refundInfo,
                                    refundDetail = item.refundDetail,
                                    journeys = transformJourneyToCancellation(it.flight.journeys, item.cancelDetail),
                                    passengers = transformPassengerToCancellation(
                                            journeyIdMapToDepartureArrivalId = journeyIdMapToDepartureArrivalId,
                                            passengerList = data.flight.passengers,
                                            cancellationDetailList = item.cancelDetail
                                    )
                            )
                    ))
                }

                cancellationList
            }

    private fun transformJourneyToCancellation(journeyList: List<OrderDetailJourney>,
                                               cancellationDetailList: List<OrderDetailCancellation.OrderDetailCancelDetail>)
            : List<FlightOrderDetailJourneyModel> {

        val journeyIdList = arrayListOf<Int>()
        val transformedJourneyList = arrayListOf<FlightOrderDetailJourneyModel>()

        journeyList.map { journey ->
            cancellationDetailList.map { cancellationDetail ->
                if (journey.id == cancellationDetail.journeyId && !journeyIdList.contains(journey.id)) {
                    transformedJourneyList.add(transformOrderDetailJourney(journey))
                    journeyIdList.add(journey.id)
                }
            }
        }

        return transformedJourneyList
    }

    private fun transformPassengerToCancellation(journeyIdMapToDepartureArrivalId: Map<Int, Pair<String, String>>,
                                                 passengerList: List<OrderDetailPassenger>,
                                                 cancellationDetailList: List<OrderDetailCancellation.OrderDetailCancelDetail>)
            : List<FlightOrderCancellationDetailPassengerModel> {
        val transformedData = arrayListOf<FlightOrderCancellationDetailPassengerModel>()

        cancellationDetailList.map { cancellationDetail ->
            passengerList.map { passenger ->
                if (cancellationDetail.passengerId == passenger.id) {
                    transformedData.add(FlightOrderCancellationDetailPassengerModel(
                            id = passenger.id,
                            type = passenger.type,
                            typeString = getPassengerTypeString(passenger.type),
                            title = passenger.title,
                            titleString = getPassengerTitleString(passenger.title),
                            firstName = passenger.firstName,
                            lastName = passenger.lastName,
                            departureAirportId = journeyIdMapToDepartureArrivalId[cancellationDetail.journeyId]?.first
                                    ?: "",
                            arrivalAirportId = journeyIdMapToDepartureArrivalId[cancellationDetail.journeyId]?.second
                                    ?: "",
                            journeyId = cancellationDetail.journeyId,
                            amenities = passenger.amenities.map { amenity ->
                                FlightOrderDetailAmenityModel(
                                        departureId = amenity.departureId,
                                        arrivalId = amenity.arrivalId,
                                        type = amenity.type,
                                        price = amenity.price,
                                        priceNumeric = amenity.priceNumeric,
                                        detail = amenity.detail
                                )
                            }.toList()
                    ))
                }
            }
        }

        return transformedData
    }

    companion object {
        private const val PARAM_INVOICE_ID = "invoiceID"
        private const val PARAM_DATA = "data"
    }

}