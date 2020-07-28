package com.tokopedia.buyerorder.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonParam
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelParam
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import com.tokopedia.buyerorder.detail.domain.BuyerInstantCancelUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerGetCancellationReasonViewModel @Inject constructor(dispatcher: BuyerDispatcherProvider,
                                                              private val getCancellationReasonUseCase: BuyerGetCancellationReasonUseCase,
                                                              private val buyerInstantCancelUseCase: BuyerInstantCancelUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _cancelReasonResult = MutableLiveData<Result<BuyerGetCancellationReasonData.Data>>()
    val cancelReasonResult: LiveData<Result<BuyerGetCancellationReasonData.Data>>
        get() = _cancelReasonResult

    private val _buyerInstantCancelResult = MutableLiveData<Result<BuyerInstantCancelData.Data>>()
    val buyerInstantCancelResult: LiveData<Result<BuyerInstantCancelData.Data>>
        get() = _buyerInstantCancelResult


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
}