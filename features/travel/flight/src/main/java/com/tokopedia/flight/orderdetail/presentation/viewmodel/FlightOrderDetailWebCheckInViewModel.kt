package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 14/11/2020
 */
class FlightOrderDetailWebCheckInViewModel @Inject constructor(private val orderDetailUseCase: FlightOrderDetailUseCase,
                                                               private val flightAnalytics: FlightAnalytics,
                                                               private val userSession: UserSessionInterface,
                                                               private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var orderId: String = ""

    private val mutableOrderDetailData = MutableLiveData<Result<FlightOrderDetailDataModel>>()
    val orderDetailData: LiveData<Result<FlightOrderDetailDataModel>>
        get() = mutableOrderDetailData

    fun trackOnCheckInDeparture(journey: FlightOrderDetailJourneyModel, isDeparture: Boolean) {
        val orderData = orderDetailData.value
        if (orderData != null && orderData is Success) {
            val airlineName = if (journey.airlineName.isNotEmpty()) journey.airlineName else "Multi Maskapai"
            val departureDate = FlightDateUtil.formatDate(FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, FlightDateUtil.YYYYMMDD, journey.departureTime)
            flightAnalytics.eventClickOnWebCheckIn(
                    "${journey.departureId}${journey.arrivalId} - $airlineName - $departureDate - $orderId",
                    userSession.userId,
                    isDeparture
            )
        }
    }

    fun fetchOrderDetailData() {
        launchCatchError(dispatcherProvider.main, block = {
            val orderDetailData = orderDetailUseCase.execute(orderId)
            orderDetailData.let {
                it.journeys.map { journey ->
                    journey.airlineLogo = getAirlineLogo(journey)
                    journey.airlineName = getAirlineName(journey)
                    journey.refundableInfo = false
                    journey.departureDateAndTime = getDepartureDateAndTime(journey)
                }
            }
            mutableOrderDetailData.postValue(Success(orderDetailData))
        }) {
            it.printStackTrace()
            mutableOrderDetailData.postValue(Fail(it))
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