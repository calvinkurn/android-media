package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.common.domain.model.SomRejectRequestParam
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.common.util.BulkRequestPickupStatus
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.ArrayList

class SomListViewModel @Inject constructor(
    somAcceptOrderUseCase: SomAcceptOrderUseCase,
    somRejectOrderUseCase: SomRejectOrderUseCase,
    somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
    somEditRefNumUseCase: SomEditRefNumUseCase,
    somValidateOrderUseCase: SomValidateOrderUseCase,
    userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers,
    private val somListGetTickerUseCase: SomListGetTickerUseCase,
    private val somListGetFilterListUseCase: SomListGetFilterListUseCase,
    private val somListGetWaitingPaymentUseCase: SomListGetWaitingPaymentUseCase,
    private val somListGetOrderListUseCase: SomListGetOrderListUseCase,
    private val somListGetTopAdsCategoryUseCase: SomListGetTopAdsCategoryUseCase,
    private val bulkAcceptOrderStatusUseCase: SomListGetBulkAcceptOrderStatusUseCase,
    private val bulkAcceptOrderUseCase: SomListBulkAcceptOrderUseCase,
    private val bulkRequestPickupUseCase: SomListBulkRequestPickupUseCase,
    private val bulkShippingStatusUseCase: SomListGetMultiShippingStatusUseCase,
    authorizeSomListAccessUseCase: AuthorizeAccessUseCase,
    authorizeMultiAcceptAccessUseCase: AuthorizeAccessUseCase
) : SomOrderBaseViewModel(
    dispatcher, userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
    somEditRefNumUseCase, somRejectCancelOrderRequest, somValidateOrderUseCase,
    authorizeSomListAccessUseCase, authorizeMultiAcceptAccessUseCase
) {

    companion object {
        private const val MAX_RETRY_GET_ACCEPT_ORDER_STATUS = 20
        private const val MAX_RETRY_GET_REQUEST_PICKUP_STATUS = 10
        private const val MAX_RETRY_REQUEST_PICKUP_USER = 1

        private const val DELAY_GET_ACCEPT_ORDER_STATUS = 1000L
        private const val DELAY_GET_MULTI_SHIPPING_STATUS = 1000L
    }

    private var retryCount = 0

    private var retryRequestPickup = 0
    private var retryRequestPickupUser = 0

    private var getOrderListJob: Job? = null
    private var getFiltersJob: Job? = null
    private var refreshOrderJobs: ArrayList<RefreshOrder> = arrayListOf()

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

    private val _refreshOrderResult = MutableLiveData<Result<OptionalOrderData>>()
    val refreshOrderResult: LiveData<Result<OptionalOrderData>>
        get() = _refreshOrderResult

    private val _topAdsCategoryResult = MutableLiveData<Result<Int>>()
    val topAdsCategoryResult: LiveData<Result<Int>>
        get() = _topAdsCategoryResult

    private val _bulkAcceptOrderResult = MutableLiveData<Result<SomListBulkAcceptOrderUiModel>>()
    val bulkAcceptOrderResult: LiveData<Result<SomListBulkAcceptOrderUiModel>>
        get() = _bulkAcceptOrderResult

    private val _bulkRequestPickupResult =
        MutableLiveData<Result<SomListBulkRequestPickupUiModel>>()
    val bulkRequestPickupResult: LiveData<Result<SomListBulkRequestPickupUiModel>>
        get() = _bulkRequestPickupResult

    private val _isLoadingOrder = MutableLiveData<Boolean>()
    val isLoadingOrder: LiveData<Boolean>
        get() = _isLoadingOrder

    private val _refreshOrderRequest = MutableLiveData<Pair<String, String>>()
    val refreshOrderRequest: LiveData<Pair<String, String>>
        get() = _refreshOrderRequest

    private val _isOrderManageEligible = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val isOrderManageEligible: LiveData<Result<Pair<Boolean, Boolean>>>
        get() = _isOrderManageEligible

    private val _canShowOrderData = MediatorLiveData<Boolean>().apply {
        value = true
        addSource(_isOrderManageEligible) { result ->
            when (result) {
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

    private var lastBulkAcceptOrderStatusSuccessResult: Result<SomListBulkAcceptOrderStatusUiModel>? =
        null
    val bulkAcceptOrderStatusResult =
        MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>()

    private val _bulkAcceptOrderStatusResult =
        MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>().apply {
            addSource(_bulkAcceptOrderResult) {
                when (it) {
                    is Success -> getBulkAcceptOrderStatus(it.data.data.batchId, 0L)
                }
            }
        }

    val bulkRequestPickupFinalResultMediator = MediatorLiveData<BulkRequestPickupResultState>()
    val bulkRequestPickupFinalResult: LiveData<BulkRequestPickupResultState>
        get() = bulkRequestPickupFinalResultMediator

    val bulkRequestPickupStatusResult =
        MediatorLiveData<Result<MultiShippingStatusUiModel>>().apply {
            addSource(_bulkRequestPickupResult) {
                when (it) {
                    is Success -> {
                        getMultiShippingStatus(it.data.data.jobId, 0L)
                    }
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
                            bulkAcceptOrderStatusResult.postValue(
                                lastBulkAcceptOrderStatusSuccessResult
                            )
                        } else if (retryCount < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                            retryCount++
                            getBulkAcceptOrderStatus(
                                (_bulkAcceptOrderResult.value as Success).data.data.batchId,
                                DELAY_GET_ACCEPT_ORDER_STATUS
                            )
                        } else {
                            bulkAcceptOrderStatusResult.postValue(
                                lastBulkAcceptOrderStatusSuccessResult
                            )
                        }
                    }
                    is Fail -> {
                        lastBulkAcceptOrderStatusSuccessResult?.apply {
                            if (this is Success) data.data.shouldRecheck = true
                        }
                        if (retryCount < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                            retryCount++
                            getBulkAcceptOrderStatus(
                                (_bulkAcceptOrderResult.value as Success).data.data.batchId,
                                DELAY_GET_ACCEPT_ORDER_STATUS
                            )
                        } else {
                            bulkAcceptOrderStatusResult.postValue(
                                lastBulkAcceptOrderStatusSuccessResult
                            )
                        }
                    }
                }
            }
        }

        bulkRequestPickupFinalResultMediator.addSource(bulkRequestPickupStatusResult) {
            when (it) {
                is Success -> {
                    val requestPickupUiModel =
                        (_bulkRequestPickupResult.value as? Success)?.data
                    val orderIdListFail =
                        it.data.listError.map { listError -> listError.orderId.toString() }
                    val totalNotEligible = requestPickupUiModel?.errors?.size?.toLong().orZero()
                    val totalOrderIds =
                        requestPickupUiModel?.data?.totalOnProcess.orZero() + totalNotEligible

                    if (it.data.success == it.data.total_order && totalOrderIds == it.data.total_order && it.data.success > 0) {
                        // case 2 When All Orders Success
                        bulkRequestPickupFinalResultMediator.postValue(AllSuccess(it.data.success))
                    } else if (it.data.total_order != it.data.processed && retryRequestPickup < MAX_RETRY_GET_REQUEST_PICKUP_STATUS) {
                        retryRequestPickup++
                        getMultiShippingStatus(
                            requestPickupUiModel?.data?.jobId.orEmpty(),
                            DELAY_GET_MULTI_SHIPPING_STATUS
                        )
                    } else {
                        // case 3 when partial success but there's not eligible and failed
                        if (it.data.success > 0 && it.data.fail > 0 && totalNotEligible > 0) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                PartialSuccessNotEligibleFail(
                                    it.data.success,
                                    totalNotEligible,
                                    orderIdListFail
                                )
                            )
                        }
                        // case 4 when All Fail but there's not eligible
                        else if (it.data.fail == requestPickupUiModel?.data?.totalOnProcess && it.data.fail > 0 && totalNotEligible > 0) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                NotEligibleAndFail(
                                    totalNotEligible,
                                    orderIdListFail
                                )
                            )
                        }
                        // case 5 When partial success but there's failed
                        else if (it.data.success > 0 && it.data.fail > 0 && totalNotEligible == 0L) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                PartialSuccess(
                                    it.data.success,
                                    orderIdListFail
                                )
                            )
                        }
                        // case 6 When Partial success but there's not eligible
                        else if (it.data.success > 0 && it.data.fail == 0L && totalNotEligible > 0) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                PartialSuccessNotEligible(
                                    it.data.success,
                                    totalNotEligible
                                )
                            )
                        }
                        //case 7 will happen fail bulk process due to all validation failed
                        else if (it.data.success == 0L && (requestPickupUiModel?.status == BulkRequestPickupStatus.SUCCESS_NOT_PROCESSED)) {
                            bulkRequestPickupFinalResultMediator.postValue(AllValidationFail)
                        }
                        //case 8 when All Not Eligible, total fail & success always 0
                        else if (totalNotEligible > 0L && it.data.fail == 0L && it.data.success == 0L) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                AllNotEligible
                            )
                        }
                        //case 9 when All Fail Eligible and should be retry the first time
                        else if (it.data.fail == it.data.total_order && it.data.fail > 0 &&
                            totalNotEligible == 0L && it.data.success == 0L && retryRequestPickupUser < MAX_RETRY_REQUEST_PICKUP_USER
                        ) {
                            retryRequestPickupUser++
                            bulkRequestPickupFinalResultMediator.postValue(
                                AllFailEligible(
                                    orderIdListFail
                                )
                            )
                        } else {
                            if (retryRequestPickupUser >= MAX_RETRY_REQUEST_PICKUP_USER) {
                                retryRequestPickupUser = 0
                            }
                            //Case 10 will happen when after 10x retry is still fail
                            bulkRequestPickupFinalResultMediator.postValue(FailRetry)
                        }
                    }
                }
                is Fail -> {
                    val requestPickupUiModel =
                        (_bulkRequestPickupResult.value as? Success)?.data
                    if (retryRequestPickup < MAX_RETRY_GET_REQUEST_PICKUP_STATUS) {
                        retryRequestPickup++
                        getMultiShippingStatus(
                            requestPickupUiModel?.data?.jobId.orEmpty(),
                            DELAY_GET_MULTI_SHIPPING_STATUS
                        )
                    } else {
                        //Case 10 will happen when there's a server error/down from BE
                        bulkRequestPickupFinalResultMediator.postValue(ServerFail(it.throwable))
                    }
                }
            }
        }
    }

    private var getOrderListParams = SomListGetOrderListParam()

    var isMultiSelectEnabled: Boolean = false
    var containsFailedRefreshOrder: Boolean = false

    override suspend fun doAcceptOrder(orderId: String, invoice: String) {
        super.doAcceptOrder(orderId, invoice)
        getFilters(false)
        withContext(dispatcher.main) {
            _refreshOrderRequest.value = orderId to invoice
        }
    }

    override suspend fun doRejectOrder(
        rejectOrderRequestParam: SomRejectRequestParam,
        invoice: String
    ) {
        super.doRejectOrder(rejectOrderRequestParam, invoice)
        getFilters(false)
        withContext(dispatcher.main) {
            _refreshOrderRequest.value = rejectOrderRequestParam.orderId to invoice
        }
    }

    override suspend fun doEditAwb(orderId: String, shippingRef: String, invoice: String) {
        super.doEditAwb(orderId, shippingRef, invoice)
        getFilters(false)
        withContext(dispatcher.main) {
            _refreshOrderRequest.value = orderId to invoice
        }
    }

    override suspend fun doRejectCancelOrder(orderId: String, invoice: String) {
        super.doRejectCancelOrder(orderId, invoice)
        getFilters(false)
        withContext(dispatcher.main) {
            _refreshOrderRequest.value = orderId to invoice
        }
    }

    private fun getBulkAcceptOrderStatus(batchId: String, wait: Long) {
        launchCatchError(block = {
            delay(wait)
            bulkAcceptOrderStatusUseCase.setParams(
                SomListBulkGetBulkAcceptOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.shopId
                )
            )
            _bulkAcceptOrderStatusResult.postValue(Success(bulkAcceptOrderStatusUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(Fail(it))
        })
    }

    private fun getMultiShippingStatus(batchId: String, wait: Long) {
        launchCatchError(block = {
            delay(wait)
            bulkShippingStatusUseCase.setParams(batchId)
            bulkRequestPickupStatusResult.postValue(Success(bulkShippingStatusUseCase.executeOnBackground()))
        }, onError = {
            bulkRequestPickupStatusResult.postValue(Fail(it))
        })
    }

    private fun cancelAllRefreshOrderJobs() {
        refreshOrderJobs.forEach { it.job.cancel() }
        refreshOrderJobs.clear()
    }

    private fun clearCompletedRefreshOrderJob() {
        refreshOrderJobs = ArrayList(refreshOrderJobs.filterNot { it.job.isCompleted })
    }

    private fun isLoadInitialData(): Boolean {
        return getOrderListParams.nextOrderId == 0L
    }

    private fun updateLoadOrderStatus(job: Job) {
        job.invokeOnCompletion {
            launchCatchError(context = dispatcher.main, block = {
                _isLoadingOrder.value = isRefreshingOrder()
            }, onError = {
                _isLoadingOrder.value = false
            })
        }
    }


    fun bulkRequestPickup(orderIds: List<String>) {
        launchCatchError(block = {
            retryRequestPickup = 0
            bulkRequestPickupUseCase.setParams(orderIds)
            _bulkRequestPickupResult.postValue(Success(bulkRequestPickupUseCase.executeOnBackground()))
        }, onError = {
            //Case 1 will happen when there's an early error/down from BE
            _bulkRequestPickupResult.postValue(Fail(it))
        })
    }

    fun bulkAcceptOrder(orderIds: List<String>) {
        launchCatchError(block = {
            retryCount = 0
            lastBulkAcceptOrderStatusSuccessResult = null
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            _bulkAcceptOrderResult.postValue(Success(bulkAcceptOrderUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderResult.postValue(Fail(it))
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
            _tickerResult.postValue(Success(somListGetTickerUseCase.executeOnBackground()))
        }, onError = {
            _tickerResult.postValue(Fail(it))
        })
    }

    fun getFilters(refreshOrders: Boolean) {
        if (somListGetFilterListUseCase.isFirstLoad) {
            somListGetFilterListUseCase.isFirstLoad = false
            launchCatchError(context = dispatcher.main, block = {
                if (_canShowOrderData.value == true) {
                    _filterResult.value = Success(
                        somListGetFilterListUseCase.executeOnBackground(true)
                            .apply { refreshOrder = refreshOrders })
                }
            }, onError = {})
        }
        launchCatchError(context = dispatcher.main, block = {
            if (_canShowOrderData.value == true) {
                _filterResult.value = Success(
                    somListGetFilterListUseCase.executeOnBackground(false)
                        .apply { refreshOrder = refreshOrders })
            }
        }, onError = {
            _filterResult.value = Fail(it)
        })
    }

    fun getWaitingPaymentCounter() {
        launchCatchError(block = {
            if (_canShowOrderData.value == true) {
                _waitingPaymentCounterResult.postValue(Success(somListGetWaitingPaymentUseCase.executeOnBackground()))
            }
        }, onError = {
            _waitingPaymentCounterResult.postValue(Fail(it))
        })
    }

    fun getOrderList() {
        if (isLoadInitialData()) {
            containsFailedRefreshOrder = false
            cancelAllRefreshOrderJobs()
        }
        getOrderListJob?.cancel()
        getOrderListJob = launchCatchError(block = {
            if (_canShowOrderData.value == true) {
                val params = somListGetOrderListUseCase.composeParams(getOrderListParams)
                val result = somListGetOrderListUseCase.executeOnBackground(params)
                getOrderListParams.nextOrderId = result.first.toLongOrZero()
                _orderListResult.postValue(Success(result.second))
            }
        }, onError = {
            _orderListResult.postValue(Fail(it))
        }).apply { updateLoadOrderStatus(this) }
    }

    fun refreshSelectedOrder(orderId: String, invoice: String) {
        if (!isRefreshingAllOrder()) {
            var refreshOrder: RefreshOrder? = null
            val job = launchCatchError(block = {
                val getOrderListParams = getOrderListParams.copy(
                    search = invoice,
                    nextOrderId = 0L
                )
                val params = somListGetOrderListUseCase.composeParams(getOrderListParams)
                val result = somListGetOrderListUseCase.executeOnBackground(params)
                getFiltersJob?.join()
                withContext(dispatcher.main) {
                    refreshOrderJobs.remove(refreshOrder)
                    _refreshOrderResult.value =
                        Success(OptionalOrderData(orderId, result.second.firstOrNull()))
                }
            }, onError = {
                withContext(dispatcher.main) {
                    _refreshOrderResult.value = Fail(it)
                    containsFailedRefreshOrder = true
                }
            }).apply { updateLoadOrderStatus(this) }
            refreshOrder = RefreshOrder(orderId, invoice, job)
            refreshOrderJobs.add(refreshOrder)
        }
    }

    fun retryRefreshSelectedOrder() {
        val jobs = refreshOrderJobs.filter { it.job.isCompleted }
        clearCompletedRefreshOrderJob()
        jobs.forEach {
            refreshSelectedOrder(it.orderId, it.invoice)
        }
    }

    fun getTopAdsCategory() {
        launchCatchError(block = {
            somListGetTopAdsCategoryUseCase.setParams(userSession.shopId.toIntOrZero())
            _topAdsCategoryResult.postValue(Success(somListGetTopAdsCategoryUseCase.executeOnBackground()))
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
        getOrderListParams.nextOrderId = 0L
    }

    fun hasNextPage(): Boolean = getOrderListParams.nextOrderId != 0L

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

    fun isRefreshingAllOrder() = getOrderListJob?.isCompleted == false

    fun isRefreshingSelectedOrder() = refreshOrderJobs.any { !it.job.isCompleted }

    fun isRefreshingOrder() = isRefreshingAllOrder() || isRefreshingSelectedOrder()

    fun isOrderStatusIdsChanged(orderStatusIds: List<Int>): Boolean {
        return this.getOrderListParams.statusList != orderStatusIds
    }

    fun getAdminPermission() {
        launchCatchError(
            block = {
                _isOrderManageEligible.postValue(
                    getAdminAccessEligibilityPair(
                        AccessId.SOM_LIST,
                        AccessId.SOM_MULTI_ACCEPT
                    )
                )
            },
            onError = {
                _isOrderManageEligible.postValue(Fail(it))
            }
        )
    }
}