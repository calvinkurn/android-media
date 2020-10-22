package com.tokopedia.flight.orderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel(private val orderDetailUseCase: FlightOrderDetailUseCase,
                                 private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {


    private val mutableOrderDetailData = MutableLiveData<Result<OrderDetailDataModel>>()
    val orderDetailData: LiveData<Result<OrderDetailDataModel>>
        get() = mutableOrderDetailData

    fun fetchOrderDetailData(invoiceId: String) {
        launchCatchError(dispatcherProvider.ui(), block = {
            val orderDetailData = orderDetailUseCase.execute(invoiceId)
            mutableOrderDetailData.postValue(Success(orderDetailData))
        }) {
            it.printStackTrace()
            mutableOrderDetailData.postValue(Fail(it))
        }
    }

}