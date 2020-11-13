package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.TravelCrossSellingGQLQuery
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailGetInvoiceEticketUseCase
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                     private val orderDetailUseCase: FlightOrderDetailUseCase,
                                                     private val getInvoiceEticketUseCase: FlightOrderDetailGetInvoiceEticketUseCase,
                                                     private val crossSellUseCase: TravelCrossSellingUseCase,
                                                     private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

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

    fun getUserEmail(): String = if (userSession.isLoggedIn) userSession.email else ""

    fun fetchOrderDetailData() {
        launchCatchError(dispatcherProvider.ui(), block = {
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
        launchCatchError(dispatcherProvider.ui(), block = {
            val eticketData = getInvoiceEticketUseCase.executeGetETicket(orderId)
            if (eticketData.isNotEmpty()) mutableETicketData.postValue(Success(eticketData))
        }) {
            it.printStackTrace()
            mutableETicketData.postValue(Fail(it))
        }
    }

    fun fetchCrossSellData() {
        launch(dispatcherProvider.ui()) {
            mutableCrossSell.postValue(crossSellUseCase.execute(TravelCrossSellingGQLQuery.QUERY_CROSS_SELLING,
                    orderId, TravelCrossSellingUseCase.PARAM_FLIGHT_PRODUCT))
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