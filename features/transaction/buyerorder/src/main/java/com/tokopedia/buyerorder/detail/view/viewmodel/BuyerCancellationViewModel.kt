package com.tokopedia.buyerorder.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelParam
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerRequestCancelUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerCancellationViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                     private val getCancellationReasonUseCase: BuyerGetCancellationReasonUseCase,
                                                     private val buyerInstantCancelUseCase: BuyerInstantCancelUseCase,
                                                     private val buyerRequestCancelUseCase: BuyerRequestCancelUseCase) : BaseViewModel(dispatcher.main) {

    private val _cancelReasonResult = MutableLiveData<Result<BuyerGetCancellationReasonData.Data>>()
    val cancelReasonResult: LiveData<Result<BuyerGetCancellationReasonData.Data>>
        get() = _cancelReasonResult

    private val _buyerInstantCancelResult = MutableLiveData<Result<BuyerInstantCancelData.Data>>()
    val buyerInstantCancelResult: LiveData<Result<BuyerInstantCancelData.Data>>
        get() = _buyerInstantCancelResult

    private val _requestCancelResult = MutableLiveData<Result<BuyerRequestCancelData.Data>>()
    val requestCancelResult: LiveData<Result<BuyerRequestCancelData.Data>>
        get() = _requestCancelResult

    fun getCancelReasons(cancelReasonQuery: String, userId: String, orderId: String) {
        launch {
            _cancelReasonResult.postValue(getCancellationReasonUseCase.execute(cancelReasonQuery, BuyerGetCancellationReasonParam(userId = userId, orderId = orderId)))
        }
    }

    fun instantCancellation(instantCancelQuery: String, orderId: String, reasonCode: String, reasonStr: String) {
        launch {
            _buyerInstantCancelResult.postValue(buyerInstantCancelUseCase.execute(instantCancelQuery, BuyerInstantCancelParam(orderId = orderId, reasonCode = reasonCode, reason = reasonStr)))
        }
    }

    fun requestCancel(requestCancelQuery: String, userId: String, orderId: String, reasonCode: String, reasonStr: String) {
        launch {
            _requestCancelResult.postValue(buyerRequestCancelUseCase.execute(requestCancelQuery, BuyerRequestCancelParam(userId = userId, orderId = orderId, reasonCode = reasonCode, reason = reasonStr)))
        }
    }
}