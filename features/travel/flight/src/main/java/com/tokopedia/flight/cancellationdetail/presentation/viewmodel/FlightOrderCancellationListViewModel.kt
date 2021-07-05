package com.tokopedia.flight.cancellationdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationListViewModel @Inject constructor(
        private val orderDetailUseCase: FlightOrderDetailUseCase,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var orderId: String = ""

    private val mutableCancellationList = MutableLiveData<Result<List<FlightOrderCancellationListModel>>>()
    val cancellationList: LiveData<Result<List<FlightOrderCancellationListModel>>>
        get() = mutableCancellationList


    fun fetchCancellationData() {
        if (orderId.isNotEmpty()) {
            launchCatchError(dispatcherProvider.main, block = {
                val cancellationData = orderDetailUseCase.executeGetCancellationList(orderId)
                mutableCancellationList.postValue(Success(cancellationData))
            }) {
                it.printStackTrace()
                mutableCancellationList.postValue(Fail(it))
            }
        }
    }

}