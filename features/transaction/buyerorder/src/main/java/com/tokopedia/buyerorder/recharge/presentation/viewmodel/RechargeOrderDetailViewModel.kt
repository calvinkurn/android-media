package com.tokopedia.buyerorder.recharge.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.domain.RechargeOrderDetailUseCase
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailViewModel @Inject constructor(
        private val orderDetailUseCase: RechargeOrderDetailUseCase,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _orderDetailData = MutableLiveData<Result<RechargeOrderDetailModel>>()
    val orderDetailData: LiveData<Result<RechargeOrderDetailModel>>
        get() = _orderDetailData

    fun fetchData(requestParams: RechargeOrderDetailRequest) {
        launchCatchError(block = {
            val orderDetailDeferred = fetchOrderDetailData(requestParams)

            _orderDetailData.postValue(orderDetailDeferred.await())
        }) {
            it.printStackTrace()
        }
    }

    private fun fetchOrderDetailData(requestParams: RechargeOrderDetailRequest) =
            async {
                orderDetailUseCase.execute(requestParams)
            }

}