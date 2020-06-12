package com.tokopedia.buyerorder.detail.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.detail.domain.BuyerGetCancellationReasonUseCase
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/06/20.
 */
class BuyerGetCancellationReasonViewModel @Inject constructor(dispatcher: BuyerDispatcherProvider,
                                                              private val getCancellationReasonUseCase: BuyerGetCancellationReasonUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderDetailResult = MutableLiveData<Result<SomDetailOrder.Data.GetSomDetail>>()
    val orderDetailResult: LiveData<Result<SomDetailOrder.Data.GetSomDetail>>
        get() = _orderDetailResult

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrder.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrder.Data>>
        get() = _acceptOrderResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrder.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrder.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditAwbResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditAwbResponse.Data>>
        get() = _editRefNumResult

    private val _setDelivered = MutableLiveData<Result<SetDeliveredResponse>>()
    val setDelivered: LiveData<Result<SetDeliveredResponse>>
        get() = _setDelivered

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launch {
            _orderDetailResult.postValue(somGetOrderDetailUseCase.execute(detailQuery, orderId))
        }
    }

    fun acceptOrder(acceptOrderQuery: String, orderId: String, shopId: String) {
        launch {
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute(acceptOrderQuery, orderId, shopId))
        }
    }

    fun getRejectReasons(rejectReasonQuery: String) {
        launch {
            _rejectReasonResult.postValue(somReasonRejectUseCase.execute(rejectReasonQuery, SomReasonRejectParam()))
        }
    }

    fun rejectOrder(rejectOrderQuery: String, rejectOrderRequest: SomRejectRequest) {
        launch {
            _rejectOrderResult.postValue(somRejectOrderUseCase.execute(rejectOrderQuery, rejectOrderRequest))
        }
    }

    fun editAwb(queryString: String) {
        launch {
            _editRefNumResult.postValue(somEditRefNumUseCase.execute(queryString))
        }
    }

    fun setDelivered(rawQuery: String, orderId: String, receivedBy: String) {
        launch {
            _setDelivered.postValue(somSetDeliveredUseCase.execute(rawQuery, orderId, receivedBy))
        }
    }
}