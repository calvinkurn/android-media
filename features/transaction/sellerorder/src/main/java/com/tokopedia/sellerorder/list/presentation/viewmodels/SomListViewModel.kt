package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
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
import kotlinx.coroutines.launch
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
        private const val DELAY_BULK_REQUEST_PICK_UP = 500L
    }

    private var retryRequestPickup = Int.ZERO
    private var retryRequestPickupUser = Int.ZERO

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
                else -> {
                    value = false
                }
            }
        }
    }

    private var lastBulkAcceptOrderStatusSuccessResult: Result<SomListBulkAcceptOrderStatusUiModel>? = null
    val bulkAcceptOrderStatusResult = MediatorLiveData<Result<SomListBulkAcceptOrderStatusUiModel>>()

    private val _bulkAcceptOrderStatusResult = MediatorLiveData<Pair<Int, Result<SomListBulkAcceptOrderStatusUiModel>>>().apply {
        addSource(_bulkAcceptOrderResult) {
            if (it is Success) getBulkAcceptOrderStatus(Int.ZERO)
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
                        //case 3 when All Not Eligible, total fail & success always 0
                        val totalNotEligible = it.data.errors.size.toLong()
                        if (it.data.data.totalOnProcess == Int.ZERO && totalNotEligible > Int.ZERO) {
                            bulkRequestPickupFinalResultMediator.postValue(
                                AllNotEligible
                            )
                        } else {
                            getMultiShippingStatus(it.data.data.jobId, 0L)
                        }
                    }
                }
            }
        }

    init {
        bulkAcceptOrderStatusResult.addSource(_bulkAcceptOrderStatusResult) {
            onReceiveBulkAcceptOrderStatusResult(it)
        }

        bulkRequestPickupFinalResultMediator.addSource(bulkRequestPickupStatusResult) {
            onReceiveBulkRequestPickupStatusResult(it)
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

    private fun getSuccessBulkAcceptOrderResult() = _bulkAcceptOrderResult.value as Success

    private fun getBulkAcceptOrderStatus(retryCount: Int) {
        launchCatchError(block = {
            delay(DELAY_GET_ACCEPT_ORDER_STATUS)
            val batchId = getSuccessBulkAcceptOrderResult().data.data.batchId
            bulkAcceptOrderStatusUseCase.setParams(
                SomListBulkGetBulkAcceptOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.shopId
                )
            )
            _bulkAcceptOrderStatusResult.postValue(retryCount + 1 to Success(bulkAcceptOrderStatusUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderStatusResult.postValue(retryCount + 1 to Fail(it))
        })
    }

    private fun onReceiveBulkAcceptOrderStatusResult(result: Pair<Int, Result<SomListBulkAcceptOrderStatusUiModel>>?) {
        when (val resultData = result?.second) {
            is Success -> {
                lastBulkAcceptOrderStatusSuccessResult = resultData.apply {
                    data.data.shouldRecheck = false
                }
                if (resultData.data.data.success + resultData.data.data.fail == resultData.data.data.totalOrder) {
                    bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                } else if (result.first < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                    getBulkAcceptOrderStatus(result.first)
                } else {
                    bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                }
            }
            is Fail -> {
                if (result.first < MAX_RETRY_GET_ACCEPT_ORDER_STATUS) {
                    lastBulkAcceptOrderStatusSuccessResult.let {
                        if (it is Success) {
                            it.data.data.shouldRecheck = true
                        }
                    }
                    getBulkAcceptOrderStatus(result.first)
                } else {
                    var lastBulkAcceptOrderStatusSuccessResult = this.lastBulkAcceptOrderStatusSuccessResult
                    if (lastBulkAcceptOrderStatusSuccessResult == null) {
                        lastBulkAcceptOrderStatusSuccessResult = resultData
                    }
                    bulkAcceptOrderStatusResult.postValue(lastBulkAcceptOrderStatusSuccessResult)
                }
            }
        }
    }

    private fun onReceiveBulkRequestPickupStatusResult(result: Result<MultiShippingStatusUiModel>?) {
        when (result) {
            is Success -> {
                val orderIdListFail = getFailingOrderIdsFromBulkRequestPickupStatus(result.data)
                val totalNotEligible = getTotalNotEligibleOrderFromBulkRequestPickupResult()
                val totalEligible = getTotalEligibleOrderFromBulkRequestPickupResult()
                val totalOrderIds = totalEligible + totalNotEligible
                val totalSuccess = result.data.success
                val totalFail = result.data.fail
                val totalOrders = result.data.total_order

                // case 3 When All Orders Success
                if (totalSuccess == totalOrders && totalOrderIds == totalOrders && totalSuccess > Int.ZERO) {
                    bulkRequestPickupFinalResultMediator.postValue(AllSuccess(totalSuccess))
                }
                //case 4 when total order != it.data.processed and retry < 10
                else if (totalOrders != result.data.processed && retryRequestPickup < MAX_RETRY_GET_REQUEST_PICKUP_STATUS) {
                    retryRequestPickup++
                    getMultiShippingStatus(
                        getBulkRequestPickupJobID(),
                        DELAY_GET_MULTI_SHIPPING_STATUS
                    )
                } else {
                    // case 5 when partial success but there's not eligible and failed
                    if (totalSuccess > Int.ZERO && totalFail > Int.ZERO && totalNotEligible > Int.ZERO &&
                        retryRequestPickupUser < MAX_RETRY_REQUEST_PICKUP_USER) {
                        retryRequestPickupUser++
                        bulkRequestPickupFinalResultMediator.postValue(
                            PartialSuccessNotEligibleFail(
                                totalSuccess,
                                totalNotEligible,
                                orderIdListFail
                            )
                        )
                    }
                    // case 6 when All Fail but there's not eligible
                    else if (totalFail == totalEligible && totalFail > Int.ZERO
                        && totalNotEligible > Int.ZERO &&
                        retryRequestPickupUser < MAX_RETRY_REQUEST_PICKUP_USER
                    ) {
                        retryRequestPickupUser++
                        bulkRequestPickupFinalResultMediator.postValue(
                            NotEligibleAndFail(
                                totalNotEligible,
                                orderIdListFail
                            )
                        )
                    }
                    // case 7 When partial success but there's failed
                    else if (totalSuccess > Int.ZERO && totalFail > Int.ZERO &&
                        totalNotEligible == Int.ZERO &&
                        retryRequestPickupUser < MAX_RETRY_REQUEST_PICKUP_USER
                    ) {
                        retryRequestPickupUser++
                        bulkRequestPickupFinalResultMediator.postValue(
                            PartialSuccess(
                                totalSuccess,
                                orderIdListFail
                            )
                        )
                    }
                    // case 8 When Partial success but there's not eligible
                    else if (totalSuccess > Int.ZERO && totalFail == Int.ZERO &&
                        totalNotEligible > Int.ZERO
                    ) {
                        bulkRequestPickupFinalResultMediator.postValue(
                            PartialSuccessNotEligible(
                                totalSuccess,
                                totalNotEligible
                            )
                        )
                    }
                    //case 9 will happen fail bulk process due to all validation failed
                    else if (getBulkRequestPickupStatus() == BulkRequestPickupStatus.SUCCESS_NOT_PROCESSED) {
                        bulkRequestPickupFinalResultMediator.postValue(AllValidationFail)
                    }
                    //case 10 when All Fail Eligible and should be retry the first time
                    else if (totalFail == totalOrders && totalFail > Int.ZERO &&
                        retryRequestPickupUser < MAX_RETRY_REQUEST_PICKUP_USER
                    ) {
                        retryRequestPickupUser++
                        bulkRequestPickupFinalResultMediator.postValue(
                            AllFailEligible(
                                orderIdListFail
                            )
                        )
                    } else {
                        if (retryRequestPickupUser >= MAX_RETRY_REQUEST_PICKUP_USER) {
                            retryRequestPickupUser = Int.ZERO
                        }
                        //Case 11 will happen when after 10x retry is still fail
                        bulkRequestPickupFinalResultMediator.postValue(FailRetry)
                    }
                }
            }
            is Fail -> {
                if (retryRequestPickup < MAX_RETRY_GET_REQUEST_PICKUP_STATUS) {
                    retryRequestPickup++
                    getMultiShippingStatus(
                        getBulkRequestPickupJobID(),
                        DELAY_GET_MULTI_SHIPPING_STATUS
                    )
                } else {
                    //Case 12 will happen when there's a server error/down from BE
                    bulkRequestPickupFinalResultMediator.postValue(ServerFail(result.throwable))
                }
            }
        }
    }

    private fun getFailingOrderIdsFromBulkRequestPickupStatus(data: MultiShippingStatusUiModel): List<String> {
        return data.listError.map { listError -> listError.orderId }
    }

    private fun getSuccessBulkRequestPickupResultData(): SomListBulkRequestPickupUiModel? {
        return _bulkRequestPickupResult.value.let {
            if (it is Success) it.data else null
        }
    }

    private fun getTotalNotEligibleOrderFromBulkRequestPickupResult(): Int {
        return getSuccessBulkRequestPickupResultData()?.let {
            it.errors.size
        }.orZero()
    }

    private fun getTotalEligibleOrderFromBulkRequestPickupResult(): Int {
        return getSuccessBulkRequestPickupResultData()?.let {
            it.data.totalOnProcess
        }.orZero()
    }

    private fun getBulkRequestPickupJobID(): String {
        return getSuccessBulkRequestPickupResultData()?.let {
            it.data.jobId
        }.orEmpty()
    }

    private fun getBulkRequestPickupStatus(): Int {
        return getSuccessBulkRequestPickupResultData()?.status.orZero()
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
            launch(context = dispatcher.main) { _isLoadingOrder.value = isRefreshingOrder() }
        }
    }

    private fun resetGetBulkAcceptOrderStatusState() {
        lastBulkAcceptOrderStatusSuccessResult = null
    }

    private suspend fun waitForGetFiltersCompleted() {
        getFiltersJob?.let {
            it.join()
        }
    }

    fun bulkRequestPickup(orderIds: List<String>) {
        launchCatchError(block = {
            delay(DELAY_BULK_REQUEST_PICK_UP)
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
            resetGetBulkAcceptOrderStatusState()
            bulkAcceptOrderUseCase.setParams(orderIds, userSession.userId)
            _bulkAcceptOrderResult.postValue(Success(bulkAcceptOrderUseCase.executeOnBackground()))
        }, onError = {
            _bulkAcceptOrderResult.postValue(Fail(it))
        })
    }

    fun retryGetBulkAcceptOrderStatus() {
        resetGetBulkAcceptOrderStatusState()
        getBulkAcceptOrderStatus(Int.ZERO)
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
                    val result = somListGetFilterListUseCase.executeOnBackground(true)
                    result.refreshOrder = refreshOrders
                    _filterResult.value = Success(result)
                }
            }, onError = {})
        }
        launchCatchError(context = dispatcher.main, block = {
            if (_canShowOrderData.value == true) {
                val result = somListGetFilterListUseCase.executeOnBackground(false)
                result.refreshOrder = refreshOrders
                _filterResult.value = Success(result)
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
                waitForGetFiltersCompleted()
                withContext(dispatcher.main) {
                    refreshOrderJobs.remove(refreshOrder)
                    _refreshOrderResult.value = Success(OptionalOrderData(orderId, result.second.firstOrNull()))
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