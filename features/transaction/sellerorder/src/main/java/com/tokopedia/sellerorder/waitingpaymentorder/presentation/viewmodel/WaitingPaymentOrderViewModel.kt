package com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.waitingpaymentorder.domain.GetWaitingPaymentOrderUseCase
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderViewModel @Inject constructor(
        dispatcher: SomDispatcherProvider,
        private val getWaitingPaymentOrderUseCase: GetWaitingPaymentOrderUseCase
) : BaseViewModel(dispatcher.ui()) {

    private val _waitingPaymentOrderResult = MutableLiveData<Result<List<WaitingPaymentOrder>>>()
    val waitingPaymentOrderResult: LiveData<Result<List<WaitingPaymentOrder>>>
        get() = _waitingPaymentOrderResult

    var paging = Paging()

    @Suppress("UNCHECKED_CAST")
    fun loadWaitingPaymentOrder(param: WaitingPaymentOrderRequestParam) {
        launchCatchError(block = {
            val result = getWaitingPaymentOrderUseCase.execute(param)
            _waitingPaymentOrderResult.postValue(Success(
                    (result[WaitingPaymentOrder::class.java.simpleName] as? List<WaitingPaymentOrder>).orEmpty()
            ))
            paging = (result[Paging::class.java.simpleName] as? Paging) ?: paging
        }, onError = {
            _waitingPaymentOrderResult.postValue(Fail(it))
        })
    }

    fun resetPaging() {
        paging = Paging()
    }
}