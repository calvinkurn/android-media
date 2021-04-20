package com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_WAITING_PAYMENT_ORDER_LIST_PAGING_RESULT
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_WAITING_PAYMENT_ORDER_LIST_RESULT
import com.tokopedia.sellerorder.waitingpaymentorder.domain.GetWaitingPaymentOrderUseCase
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val getWaitingPaymentOrderUseCase: GetWaitingPaymentOrderUseCase
) : BaseViewModel(dispatcher.io) {

    private val _waitingPaymentOrderResult = MutableLiveData<Result<List<WaitingPaymentOrderUiModel>>>()
    val waitingPaymentOrderUiModelResult: LiveData<Result<List<WaitingPaymentOrderUiModel>>>
        get() = _waitingPaymentOrderResult

    var paging = Paging()

    @Suppress("UNCHECKED_CAST")
    fun loadWaitingPaymentOrder(param: WaitingPaymentOrderRequestParam) {
        launchCatchError(block = {
            val result = getWaitingPaymentOrderUseCase.execute(param)
            _waitingPaymentOrderResult.postValue(Success(
                    (result[KEY_WAITING_PAYMENT_ORDER_LIST_RESULT] as? List<WaitingPaymentOrderUiModel>).orEmpty()
            ))
            paging = (result[KEY_WAITING_PAYMENT_ORDER_LIST_PAGING_RESULT] as? Paging) ?: paging
        }, onError = {
            _waitingPaymentOrderResult.postValue(Fail(it))
        })
    }

    fun resetPaging() {
        paging = Paging()
    }
}