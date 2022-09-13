package com.tokopedia.watch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.watch.orderlist.model.OrderListModel
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderStatusUiModel
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderUiModel
import com.tokopedia.watch.orderlist.param.SomListGetAcceptBulkOrderStatusParam
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import com.tokopedia.watch.orderlist.usecase.SomListAcceptBulkOrderUseCase
import com.tokopedia.watch.orderlist.usecase.SomListGetAcceptBulkOrderStatusUseCase
import kotlinx.coroutines.delay
import rx.Subscriber
import javax.inject.Inject

class TokopediaWatchViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val somListAcceptBulkOrderUseCase: SomListAcceptBulkOrderUseCase,
    private val somListGetAcceptBulkOrderStatusUseCase: SomListGetAcceptBulkOrderStatusUseCase,
    private val getOrderListUseCase: GetOrderListUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    companion object {
        private const val DELAY_GET_ACCEPT_ORDER_STATUS = 1000L
    }

    private var orderIds: List<String> = emptyList()

    private val _bulkAcceptOrderResult = MutableLiveData<Result<SomListAcceptBulkOrderUiModel>>()
    val bulkAcceptOrderResult: LiveData<Result<SomListAcceptBulkOrderUiModel>>
        get() = _bulkAcceptOrderResult

    private val _orderListModel = MutableLiveData<Result<OrderListModel>>()
    val orderListModel: LiveData<Result<OrderListModel>>
        get() = _orderListModel

    private val _bulkAcceptOrderStatusResult = MediatorLiveData<Pair<Int, Result<SomListAcceptBulkOrderStatusUiModel>>>().apply {
        addSource(_bulkAcceptOrderResult) {
            if (it is Success) getAcceptBulkOrderStatus(Int.ZERO)
        }
    }

    fun getOrderList() {
        launchCatchError(block = {
            getOrderListUseCase.execute(RequestParams(), getLoadOrderListDataSubscriber())
        }, onError = {

        })
    }

    fun acceptBulkOrder() {
        launchCatchError(block = {
            resetGetAcceptBulkOrderStatusState()
            somListAcceptBulkOrderUseCase.setParams(orderIds, userSession.userId)
            _bulkAcceptOrderResult.postValue(Success(somListAcceptBulkOrderUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderResult.postValue(Fail(it))
        })
    }

    private fun getAcceptBulkOrderStatus(retryCount: Int) {
        launchCatchError(block = {
            delay(DELAY_GET_ACCEPT_ORDER_STATUS)
            val batchId = getSuccessAcceptBulkOrderResult().data.data.batchId
            somListGetAcceptBulkOrderStatusUseCase.setParams(
                SomListGetAcceptBulkOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.shopId
                )
            )
            _bulkAcceptOrderStatusResult.postValue(retryCount + 1 to Success(somListGetAcceptBulkOrderStatusUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(retryCount + 1 to Fail(it))
        })
    }

    private fun getSuccessAcceptBulkOrderResult() = _bulkAcceptOrderResult.value as Success

    private fun resetGetAcceptBulkOrderStatusState() {
        lastBulkAcceptOrderStatusSuccessResult = null
    }

    private var lastBulkAcceptOrderStatusSuccessResult: Result<SomListAcceptBulkOrderStatusUiModel>? = null

    private fun getLoadOrderListDataSubscriber(): Subscriber<OrderListModel> {
        return object: Subscriber<OrderListModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(orderListModel: OrderListModel) {
                orderIds = orderListModel.orderList.list.map { it.orderId }
                _orderListModel.value = Success(orderListModel)
            }
        }
    }
}