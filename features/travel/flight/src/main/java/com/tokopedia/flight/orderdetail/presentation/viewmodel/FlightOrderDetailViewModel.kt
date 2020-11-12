package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailGetInvoiceEticketUseCase
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailJourneyModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                     private val orderDetailUseCase: FlightOrderDetailUseCase,
                                                     private val getInvoiceEticketUseCase: FlightOrderDetailGetInvoiceEticketUseCase,
                                                     private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var invoiceId: String = ""

    private val mutableOrderDetailData = MutableLiveData<Result<OrderDetailDataModel>>()
    val orderDetailData: LiveData<Result<OrderDetailDataModel>>
        get() = mutableOrderDetailData

    private val mutableETicketData = MutableLiveData<Result<String>>()
    val eticketData: LiveData<Result<String>>
        get() = mutableETicketData

    fun getUserEmail(): String = if (userSession.isLoggedIn) userSession.email else ""

    fun fetchOrderDetailData() {
        launchCatchError(dispatcherProvider.ui(), block = {
            val orderDetailData = orderDetailUseCase.execute(invoiceId)
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
            val eticketData = getInvoiceEticketUseCase.executeGetETicket(invoiceId)
            if (eticketData.isNotEmpty()) mutableETicketData.postValue(Success(eticketData))
        }) {
            it.printStackTrace()
            mutableETicketData.postValue(Fail(it))
        }
    }

    private fun getAirlineLogo(journey: OrderDetailJourneyModel): String? {
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

    private fun getAirlineName(journey: OrderDetailJourneyModel): String {
        val airlineName = arrayListOf<String>()

        for (item in journey.routes) {
            if (!airlineName.contains(item.airlineName)) {
                airlineName.add(item.airlineName)
            }
        }

        return airlineName.joinToString(" + ")
    }

    private fun getRefundableInfo(journey: OrderDetailJourneyModel): Boolean {
        var isRefundable = false

        for (item in journey.routes) {
            if (item.refundable) {
                isRefundable = true
                break
            }
        }

        return isRefundable
    }

    private fun getDepartureDateAndTime(journey: OrderDetailJourneyModel): Pair<String, String> {

        val time = "${TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                TravelDateUtil.HH_MM, journey.departureTime)} - ${TravelDateUtil.formatDate(
                TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.HH_MM, journey.arrivalTime)}"

        return Pair(
                TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, TravelDateUtil.EEE_DD_MMM_YY, journey.departureTime),
                time
        )
    }

}