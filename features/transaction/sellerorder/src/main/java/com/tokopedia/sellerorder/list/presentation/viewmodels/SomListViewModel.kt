package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.domain.model.SomListBulkGetBulkAcceptOrderStatusParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerParam
import com.tokopedia.sellerorder.list.domain.usecases.*
import com.tokopedia.sellerorder.list.presentation.models.*
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import javax.inject.Inject

class SomListViewModel @Inject constructor(
        somAcceptOrderUseCase: SomAcceptOrderUseCase,
        somRejectOrderUseCase: SomRejectOrderUseCase,
        somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
        somEditRefNumUseCase: SomEditRefNumUseCase,
        userSession: UserSessionInterface,
        dispatcher: SomDispatcherProvider,
        private val somListGetTickerUseCase: SomListGetTickerUseCase,
        private val somListGetFilterListUseCase: SomListGetFilterListUseCase,
        private val somListGetWaitingPaymentUseCase: SomListGetWaitingPaymentUseCase,
        private val somListGetOrderListUseCase: SomListGetOrderListUseCase,
        private val somListGetTopAdsCategoryUseCase: SomListGetTopAdsCategoryUseCase,
        private val bulkAcceptOrderStatusUseCase: SomListGetBulkAcceptOrderStatusUseCase,
        private val bulkAcceptOrderUseCase: SomListBulkAcceptOrderUseCase,
        private val authorizeSomListAccessUseCase: AuthorizeAccessUseCase,
        private val authorizeMultiAcceptAccessUseCase: AuthorizeAccessUseCase
) : SomOrderBaseViewModel(dispatcher.io(), userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
        somEditRefNumUseCase, somRejectCancelOrderRequest) {

    companion object {
        private const val MAX_RETRY_GET_ACCEPT_ORDER_STATUS = 20
        private const val DELAY_GET_ACCEPT_ORDER_STATUS = 1000L
    }

    private var retryCount = 0

    private var getOrderListJob: Job? = null

    private val _tickerResult = MutableLiveData<Result<List<TickerData>>>()
    val tickerResult: LiveData<Result<List<TickerData>>>
        get() = _tickerResult

    private val _filterResult = MutableLiveData<Result<SomListFilterUiModel>>()
    val filterResult: LiveData<Result<SomListFilterUiModel>>
        get() = _filterResult

    private val _waitingPaymentCounterResult = MutableLiveData<Result<WaitingPaymentCounter>>()
    val waitingPaymentCounterResult: LiveData<Result<WaitingPaymentCounter>>
        get() = _waitingPaymentCounterResult

    private val _orderListResult = MutableLiveData<Result<List<SomListOrderUiModel>>>()
    val orderListResult: LiveData<Result<List<SomListOrderUiModel>>>
        get() = _orderListResult

    private val _topAdsCategoryResult = MutableLiveData<Result<Int>>()
    val topAdsCategoryResult: LiveData<Result<Int>>
        get() = _topAdsCategoryResult

    private val _bulkAcceptOrderResult = MutableLiveData<Result<SomListBulkAcceptOrderUiModel>>()
    val bulkAcceptOrderResult: LiveData<Result<SomListBulkAcceptOrderUiModel>>
        get() = _bulkAcceptOrderResult

    private val _isOrderManageEligible = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val isOrderManageEligible: LiveData<Result<Pair<Boolean, Boolean>>>
        get() = _isOrderManageEligible

    private val _canShowOrderData = MediatorLiveData<Boolean>().apply {
        value = true
        addSource(_isOrderManageEligible) { result ->
            when(result) {
                is Success -> {
                    result.data.let { (canShowOrder, _) ->
                        value = canShowOrder
                    }
                }
                is Fail -> {
                    value = false
                }
            }
        }
    }

    private var lastBulkAcceptOrderStatusSuccessResult: Result<SomListBulkAcceptOrderStatusUiModel>? = null
    val bulkAcceptOrderStatusResult = MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>()

    private val _bulkAcceptOrderStatusResult = MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>().apply {
        addSource(_bulkAcceptOrderResult) {
            when (it) {
                is Success -> getBulkAcceptOrderStatus(it.data.data.batchId, 0L)
            }
        }
    }

    init {
        bulkAcceptOrderStatusResult.apply {
            addSource(_bulkAcceptOrderStatusResult) {
                when (it) {
                    is Success -> {
                        lastBulkAcceptOrderStatusSuccessResult = it.apply {
                            data.data.shouldRecheck = false
                        }
                        if (it.data.data.success + it.data.data.fail == it.data.data.totalOrder) {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        } else if (retryCount < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                            retryCount++
                            getBulkAcceptOrderStatus((_bulkAcceptOrderResult.value as Success).data.data.batchId, DELAY_GET_ACCEPT_ORDER_STATUS)
                        } else {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        }
                    }
                    is Fail -> {
                        lastBulkAcceptOrderStatusSuccessResult?.apply {
                            if (this is Success) data.data.shouldRecheck = true
                        }
                        if (retryCount < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                            retryCount++
                            getBulkAcceptOrderStatus((_bulkAcceptOrderResult.value as Success).data.data.batchId, DELAY_GET_ACCEPT_ORDER_STATUS)
                        } else {
                            bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                        }
                    }
                }
            }
        }
    }

    private var getOrderListParams = SomListGetOrderListParam()

    var isMultiSelectEnabled: Boolean = false

    fun bulkAcceptOrder(orderIds: List<String>) {
        launchCatchError(block = {
            retryCount = 0
            lastBulkAcceptOrderStatusSuccessResult = null
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            _bulkAcceptOrderResult.postValue(Success(bulkAcceptOrderUseCase.execute()))
        }, onError = {
            _bulkAcceptOrderResult.postValue(Fail(it))
        })
    }

    private fun getBulkAcceptOrderStatus(batchId: String, wait: Long) {
        launchCatchError(block = {
            delay(wait)
            bulkAcceptOrderStatusUseCase.setParams(SomListBulkGetBulkAcceptOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.shopId
            ))
            _bulkAcceptOrderStatusResult.postValue(Success(bulkAcceptOrderStatusUseCase.execute()))
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(Fail(it))
        })
    }

    fun retryGetBulkAcceptOrderStatus() {
        launchCatchError(block = {
            val bulkAcceptResult = _bulkAcceptOrderResult.value
            if (bulkAcceptResult is Success) {
                retryCount = 0
                lastBulkAcceptOrderStatusSuccessResult = null
                getBulkAcceptOrderStatus(bulkAcceptResult.data.data.batchId, 0L)
            }
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(Fail(it))
        })
    }

    fun getTickers() {
        launchCatchError(block = {
            somListGetTickerUseCase.setParam(SomListGetTickerParam(userId = userSession.userId))
            _tickerResult.postValue(somListGetTickerUseCase.execute())
        }, onError = {
            _tickerResult.postValue(Fail(it))
        })
    }

    fun getFilters() {
        launchCatchError(block = {
            val filterResult = somListGetFilterListUseCase.execute()
            if (_canShowOrderData.value == true) {
                _filterResult.postValue(filterResult)
            }
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun getWaitingPaymentCounter() {
        launchCatchError(block = {
            val waitingPaymentResult = somListGetWaitingPaymentUseCase.execute()
            if (_canShowOrderData.value == true) {
                _waitingPaymentCounterResult.postValue(waitingPaymentResult)
            }
        }, onError = {
            _waitingPaymentCounterResult.postValue(Fail(it))
        })
    }

    fun getOrderList() {
        getOrderListJob?.cancel()
        getOrderListJob = launchCatchError(block = {
            somListGetOrderListUseCase.setParam(getOrderListParams)
            val result = somListGetOrderListUseCase.execute()
            getOrderListParams.nextOrderId = result.first
            if (_canShowOrderData.value == true) {
                _orderListResult.postValue(Success(result.second))
            }
        }, onError = {
            _orderListResult.postValue(Fail(it))
        })
    }

    fun refreshSelectedOrder(invoice: String) {
        launchCatchError(block = {
            val currentSearchParam = getOrderListParams.search
            val currentNextOrderId = getOrderListParams.nextOrderId
            setSearchParam(invoice)
            resetNextOrderId()
            somListGetOrderListUseCase.setParam(getOrderListParams)
            val result = somListGetOrderListUseCase.execute()
            setSearchParam(currentSearchParam)
            getOrderListParams.nextOrderId = currentNextOrderId
            _orderListResult.postValue(Success(result.second))
        }, onError = {
            _orderListResult.postValue(Fail(it))
        })
    }

    fun getTopAdsCategory() {
        launchCatchError(block = {
            somListGetTopAdsCategoryUseCase.setParams(userSession.shopId.toIntOrZero())
            _topAdsCategoryResult.postValue(somListGetTopAdsCategoryUseCase.execute())
        }, onError = {
            _topAdsCategoryResult.postValue(Fail(it))
        })
    }

    fun isTopAdsActive(): Boolean {
        val topAdsGetShopInfo = topAdsCategoryResult
        if (topAdsGetShopInfo.value is Fail) return false
        val topAdsGetShopInfoSuccess = (topAdsGetShopInfo.value as? Success)?.data.orZero()
        return (topAdsGetShopInfoSuccess == SomConsts.TOPADS_MANUAL_ADS || topAdsGetShopInfoSuccess == SomConsts.TOPADS_AUTO_ADS)
    }

    fun setStatusOrderFilter(id: List<Int>) {
        getOrderListParams.statusList = id
        resetNextOrderId()
    }

    fun setSearchParam(keyword: String) {
        getOrderListParams.search = keyword
    }

    fun resetGetOrderListParam() {
        this.getOrderListParams = SomListGetOrderListParam()
    }

    fun resetNextOrderId() {
        getOrderListParams.nextOrderId = 0
    }

    fun hasNextPage(): Boolean = getOrderListParams.nextOrderId != 0

    fun getDataOrderListParams() = getOrderListParams

    fun updateGetOrderListParams(getOrderListParams: SomListGetOrderListParam) {
        this.getOrderListParams = getOrderListParams
    }

    fun setOrderTypeFilter(orderTypes: MutableSet<Int>) {
        this.getOrderListParams.orderTypeList = orderTypes
    }

    fun setSortOrderBy(value: Int) {
        this.getOrderListParams.sortBy = value
    }

    fun getAdminPermission() {
        when {
            userSession.isShopOwner -> {
                _isOrderManageEligible.postValue(Success(true to true))
                _canShowOrderData.postValue(true)
            }
            userSession.isShopAdmin -> {
                launchCatchError(
                        block = {
                            val somListRequestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toIntOrZero(), AccessId.SOM_LIST)
                            val multiAccessRequestParams = AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toIntOrZero(), AccessId.SOM_MULTI_ACCEPT)
                            async {
                                authorizeSomListAccessUseCase.execute(somListRequestParams)
                            }.let { isSomListEligibleDeferred ->
                                async {
                                    authorizeMultiAcceptAccessUseCase.execute(multiAccessRequestParams)
                                }.let { isMultiAcceptEligibleDeferred ->
                                    _isOrderManageEligible.postValue(
                                            Success(isSomListEligibleDeferred.await() to isMultiAcceptEligibleDeferred.await()))
                                }
                            }
                        },
                        onError = {
                            _isOrderManageEligible.postValue(Fail(it))
                        }
                )
            }
            else -> {
                _isOrderManageEligible.postValue(Success(false to false))
            }
        }
    }
}