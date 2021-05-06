package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.TravelCrossSellingGQLQuery
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.view.enum.FlightPassengerType
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailGetInvoiceEticketUseCase
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailAmenityEnum
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import com.tokopedia.flight.orderdetail.presentation.model.mapper.FlightOrderDetailCancellationMapper
import com.tokopedia.flight.orderdetail.presentation.model.mapper.FlightOrderDetailStatusMapper
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                     private val orderDetailUseCase: FlightOrderDetailUseCase,
                                                     private val getInvoiceEticketUseCase: FlightOrderDetailGetInvoiceEticketUseCase,
                                                     private val crossSellUseCase: TravelCrossSellingUseCase,
                                                     private val orderDetailCancellationMapper: FlightOrderDetailCancellationMapper,
                                                     private val flightAnalytics: FlightAnalytics,
                                                     private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var orderId: String = ""

    private val mutableCrossSell = MutableLiveData<Result<TravelCrossSelling>>()
    val crossSell: LiveData<Result<TravelCrossSelling>>
        get() = mutableCrossSell

    private val mutableOrderDetailData = MutableLiveData<Result<FlightOrderDetailDataModel>>()
    val orderDetailData: LiveData<Result<FlightOrderDetailDataModel>>
        get() = mutableOrderDetailData

    private val mutableETicketData = MutableLiveData<Result<String>>()
    val eticketData: LiveData<Result<String>>
        get() = mutableETicketData

    private val mutableInvoiceData = MutableLiveData<Result<String>>()
    val invoiceData: LiveData<Result<String>>
        get() = mutableInvoiceData

    private val mutableCancellationData = MutableLiveData<List<FlightCancellationJourney>>()
    val cancellationData: LiveData<List<FlightCancellationJourney>>
        get() = mutableCancellationData

    fun getUserEmail(): String = if (userSession.isLoggedIn) userSession.email else ""

    fun fetchOrderDetailData() {
        launchCatchError(dispatcherProvider.main, block = {
            val orderDetailData = orderDetailUseCase.execute(orderId)
            orderDetailData.let {
                it.journeys.map { journey ->
                    journey.airlineLogo = getAirlineLogo(journey)
                    journey.airlineName = getAirlineName(journey)
                    journey.refundableInfo = getRefundableInfo(journey)
                    journey.departureDateAndTime = getDepartureDateAndTime(journey)
                }
            }
            mutableOrderDetailData.postValue(Success(orderDetailData))
        }) {
            it.printStackTrace()
            mutableOrderDetailData.postValue(Fail(it))
        }
    }

    fun fetchETicketData() {
        launchCatchError(dispatcherProvider.main, block = {
            val eticketData = getInvoiceEticketUseCase.executeGetETicket(orderId)
            if (eticketData.isNotEmpty()) mutableETicketData.postValue(Success(eticketData))
        }) {
            it.printStackTrace()
            mutableETicketData.postValue(Fail(it))
        }
    }

    fun fetchInvoiceData() {
        launchCatchError(dispatcherProvider.main, block = {
            val invoiceData = getInvoiceEticketUseCase.executeGetInvoice(orderId)
            if (invoiceData.isNotEmpty()) mutableInvoiceData.postValue(Success(invoiceData))
        }) {
            it.printStackTrace()
            mutableInvoiceData.postValue(Fail(it))
        }
    }

    fun fetchCrossSellData() {
        launch(dispatcherProvider.main) {
            mutableCrossSell.postValue(crossSellUseCase.execute(TravelCrossSellingGQLQuery.QUERY_CROSS_SELLING,
                    orderId, TravelCrossSellingUseCase.PARAM_FLIGHT_PRODUCT))
        }
    }

    fun onNavigateToCancellationClicked(journeyList: List<FlightOrderDetailJourneyModel>) {
        mutableCancellationData.postValue(orderDetailCancellationMapper.transform(journeyList))
    }

    fun isWebCheckInAvailable(flightOrderDetailData: FlightOrderDetailDataModel): Pair<Boolean, String> {
        var checkInAvailable = false
        val today = FlightDateUtil.getCurrentDate()
        var subtitle: String = ""

        if (FlightOrderDetailStatusMapper.getStatusOrder(flightOrderDetailData.status) == FlightOrderDetailStatusMapper.SUCCESS) {
            for (journey in flightOrderDetailData.journeys) {
                val webCheckInOpenTime = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, journey.webCheckIn.startTime)
                val webCheckInCloseTime = FlightDateUtil.stringToDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, journey.webCheckIn.endTime)
                if (webCheckInOpenTime.before(today) && webCheckInCloseTime.after(today)) {
                    checkInAvailable = true
                    subtitle = journey.webCheckIn.subtitle
                    break
                }

                if (!checkInAvailable) {
                    subtitle = journey.webCheckIn.subtitle
                }
            }
        }

        return Pair(checkInAvailable, subtitle)
    }

    fun buildPaymentDetailData(): List<FlightOrderDetailSimpleModel> {
        val returnData = arrayListOf<FlightOrderDetailSimpleModel>()
        val orderDetailData = orderDetailData.value
        if (orderDetailData != null && orderDetailData is Success) {
            val passengers = hashMapOf<Int, Int>()
            for (passenger in orderDetailData.data.passengers) {
                var count = passengers[passenger.type] ?: 0
                passengers[passenger.type] = count + 1
            }

            val journeyFare = hashMapOf<String, HashMap<Int, Int>>()
            for (journey in orderDetailData.data.journeys) {
                val journeyRoute = "${journey.departureId} - ${journey.arrivalId}"
                var priceMap: HashMap<Int, Int> = hashMapOf()

                if (journey.fare.adultNumeric > 0) {
                    priceMap[FlightPassengerType.ADULT.id] = journey.fare.adultNumeric.toInt()
                }
                if (journey.fare.childNumeric > 0) {
                    priceMap[FlightPassengerType.CHILDREN.id] = journey.fare.childNumeric.toInt()
                }
                if (journey.fare.infantNumeric > 0) {
                    priceMap[FlightPassengerType.INFANT.id] = journey.fare.infantNumeric.toInt()
                }

                journeyFare[journeyRoute] = priceMap
            }

            for ((key, value) in journeyFare) {
                val adultPriceTotal = (passengers[FlightPassengerType.ADULT.id]
                        ?: 0) * (value[FlightPassengerType.ADULT.id] ?: 0)
                returnData.add(
                        FlightOrderDetailSimpleModel(
                                "$key ${FlightPassengerType.ADULT.type} x${passengers[FlightPassengerType.ADULT.id]}",
                                "Rp${CurrencyFormatHelper.convertToRupiah(adultPriceTotal.toString())}",
                                false,
                                false,
                                false,
                                false,
                                true
                        )
                )

                if (value.containsKey(FlightPassengerType.CHILDREN.id)) {
                    val childrenPriceTotal = (passengers[FlightPassengerType.CHILDREN.id]
                            ?: 0) * (value[FlightPassengerType.CHILDREN.id] ?: 0)
                    returnData.add(
                            FlightOrderDetailSimpleModel(
                                    "$key ${FlightPassengerType.CHILDREN.type} x${passengers[FlightPassengerType.CHILDREN.id]}",
                                    "Rp${CurrencyFormatHelper.convertToRupiah(childrenPriceTotal.toString())}",
                                    false,
                                    false,
                                    false,
                                    false,
                                    true
                            )
                    )
                }

                if (value.containsKey(FlightPassengerType.INFANT.id)) {
                    val leftValue = "$key ${FlightPassengerType.INFANT.type} x${passengers[FlightPassengerType.INFANT.id]}"
                    val rightValue = "Rp${CurrencyFormatHelper.convertToRupiah(value[FlightPassengerType.INFANT.id].toString())}"
                    returnData.add(
                            FlightOrderDetailSimpleModel(
                                    leftValue,
                                    rightValue,
                                    false,
                                    false,
                                    false,
                                    false,
                                    true
                            )
                    )
                }
            }
        }

        return returnData
    }

    fun buildAmenitiesPaymentDetailData(): List<FlightOrderDetailSimpleModel> {
        val returnData = arrayListOf<FlightOrderDetailSimpleModel>()
        val orderDetailData = orderDetailData.value

        if (orderDetailData != null && orderDetailData is Success) {
            for (passenger in orderDetailData.data.passengers) {
                for (amenity in passenger.amenities) {
                    if (amenity.priceNumeric > 0) {
                        val leftValue = when (amenity.type) {
                            FlightOrderDetailAmenityEnum.LUGGAGE.type -> "${FlightOrderDetailAmenityEnum.LUGGAGE.text} ${amenity.departureId} - ${amenity.arrivalId}"
                            FlightOrderDetailAmenityEnum.MEAL.type -> "${FlightOrderDetailAmenityEnum.MEAL.text} ${amenity.departureId} - ${amenity.arrivalId}"
                            else -> ""
                        }
                        val rightValue = "Rp${CurrencyFormatHelper.convertToRupiah(amenity.priceNumeric.toString())}"
                        returnData.add(
                                FlightOrderDetailSimpleModel(
                                        leftValue,
                                        rightValue,
                                        false,
                                        false,
                                        false,
                                        false,
                                        true
                                )
                        )
                    }
                }
            }
        }

        return returnData
    }

    fun getTotalAmount(): String {
        var totalAmount = ""
        val orderDetailData = orderDetailData.value

        if (orderDetailData != null && orderDetailData is Success) {
            totalAmount = "Rp${CurrencyFormatHelper.convertToRupiah(orderDetailData.data.totalPriceNumeric.toString())}"
        }

        return totalAmount
    }

    fun getOrderDetailStatus(): String =
            orderDetailData.value?.let {
                (it as Success).data.statusString
            } ?: ""

    fun trackOpenOrderDetail(statusString: String) {
        flightAnalytics.openOrderDetail(
                "$statusString - $orderId",
                userSession.userId
        )
    }

    fun trackSendETicketClicked() {
        val orderDetailData = mutableOrderDetailData.value
        if (orderDetailData != null && orderDetailData is Success) {
            flightAnalytics.eventSendETicketOrderDetail(
                    "${orderDetailData.data.statusString} - $orderId",
                    userSession.userId
            )
        }
    }

    fun trackClickWebCheckIn() {
        val orderDetailData = mutableOrderDetailData.value
        if (orderDetailData != null && orderDetailData is Success) {
            flightAnalytics.eventWebCheckInOrderDetail(
                    "${orderDetailData.data.statusString} - $orderId",
                    userSession.userId
            )
        }
    }

    fun trackClickCancel() {
        val orderDetailData = mutableOrderDetailData.value
        if (orderDetailData != null && orderDetailData is Success) {
            flightAnalytics.eventCancelTicketOrderDetail(
                    "${orderDetailData.data.statusString} - $orderId",
                    userSession.userId
            )
        }
    }

    private fun getAirlineLogo(journey: FlightOrderDetailJourneyModel): String? {
        var logoUrl = ""
        var isMultiAirline = false

        for (item in journey.routes) {
            if (logoUrl.isEmpty() || item.airlineLogo == logoUrl) {
                logoUrl = item.airlineLogo
            } else {
                isMultiAirline = true
            }
        }

        return if (!isMultiAirline && logoUrl.isNotEmpty()) logoUrl else null
    }

    private fun getAirlineName(journey: FlightOrderDetailJourneyModel): String {
        val airlineName = arrayListOf<String>()

        for (item in journey.routes) {
            if (!airlineName.contains(item.airlineName)) {
                airlineName.add(item.airlineName)
            }
        }

        return airlineName.joinToString(" + ")
    }

    private fun getRefundableInfo(journey: FlightOrderDetailJourneyModel): Boolean {
        var isRefundable = false

        for (item in journey.routes) {
            if (item.refundable) {
                isRefundable = true
                break
            }
        }

        return isRefundable
    }

    private fun getDepartureDateAndTime(journey: FlightOrderDetailJourneyModel): Pair<String, String> {

        val time = "${TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TravelDateUtil.HH_MM, journey.departureTime)} - ${TravelDateUtil.formatDate(
                TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.HH_MM, journey.arrivalTime)}"

        return Pair(
                TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.EEE_DD_MMM_YY, journey.departureTime),
                time
        )
    }

}