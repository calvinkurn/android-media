package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailJourneyModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel @Inject constructor(private val orderDetailUseCase: FlightOrderDetailUseCase,
                                                     private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var invoiceId: String = ""

    private val mutableOrderDetailData = MutableLiveData<Result<OrderDetailDataModel>>()
    val orderDetailData: LiveData<Result<OrderDetailDataModel>>
        get() = mutableOrderDetailData

    fun fetchOrderDetailData() {
        launchCatchError(dispatcherProvider.ui(), block = {
            val orderDetailData = orderDetailUseCase.execute(invoiceId)
            mutableOrderDetailData.postValue(Success(orderDetailData))
        }) {
            it.printStackTrace()
            mutableOrderDetailData.postValue(Fail(it))
        }
    }

    fun getAirlineLogo(journey: OrderDetailJourneyModel): String? {
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

    fun getAirlineName(journey: OrderDetailJourneyModel): String {
        val airlineName = arrayListOf<String>()

        for (item in journey.routes) {
            if (!airlineName.contains(item.airlineName)) {
                airlineName.add(item.airlineName)
            }
        }

        return airlineName.joinToString(" + ")
    }

    fun getRefundableInfo(journey: OrderDetailJourneyModel): Boolean {
        var isRefundable = false

        for (item in journey.routes) {
            if (item.refundable) {
                isRefundable = true
                break
            }
        }

        return isRefundable
    }

}