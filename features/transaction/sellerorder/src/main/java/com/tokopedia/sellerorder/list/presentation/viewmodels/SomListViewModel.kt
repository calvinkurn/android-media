package com.tokopedia.sellerorder.list.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.*
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.domain.model.SomListGetOrderListParam
import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerParam
import com.tokopedia.sellerorder.list.domain.usecases.*
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.sellerorder.list.presentation.models.WaitingPaymentCounter
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SomListViewModel @Inject constructor(
        getUserRoleUseCase: SomGetUserRoleUseCase,
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
        private val somListGetTopAdsCategoryUseCase: SomListGetTopAdsCategoryUseCase
) : SomOrderBaseViewModel(dispatcher.io(), userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
        somEditRefNumUseCase, somRejectCancelOrderRequest, getUserRoleUseCase) {

    companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }

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

    private var getOrderListParams = SomListGetOrderListParam().apply {
        startDate = Utils.getFormattedDate(90, DATE_FORMAT)
        endDate = Utils.getFormattedDate(0, DATE_FORMAT)
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
            _filterResult.postValue(somListGetFilterListUseCase.execute())
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun getWaitingPaymentCounter() {
        launchCatchError(block = {
            _waitingPaymentCounterResult.postValue(somListGetWaitingPaymentUseCase.execute())
        }, onError = {
            _waitingPaymentCounterResult.postValue(Fail(it))
        })
    }

    fun getOrderList() {
        launchCatchError(block = {
            somListGetOrderListUseCase.setParam(getOrderListParams)
            val result = somListGetOrderListUseCase.execute()
            getUserRolesJob?.join()
            getOrderListParams.nextOrderId = result.first
            _orderListResult.postValue(Success(result.second))
        }, onError = {
            _orderListResult.postValue(Fail(it))
        })
    }

    fun refreshSelectedOrder(invoice: String) {
        launchCatchError(block = {
            val currentSearchParam = getOrderListParams.search
            setSearchParam(invoice)
            somListGetOrderListUseCase.setParam(getOrderListParams)
            val result = somListGetOrderListUseCase.execute()
            setSearchParam(currentSearchParam)
            getUserRolesJob?.join()
            getOrderListParams.nextOrderId = result.first
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

    fun clearUserRoles() {
        _userRoleResult.postValue(null)
    }

    fun isTopAdsActive(): Boolean {
        val topAdsGetShopInfo = topAdsCategoryResult
        if (topAdsGetShopInfo.value is Fail) return false
        return (topAdsGetShopInfo.value as? Success)?.data.orZero() == SomConsts.TOPADS_MANUAL_ADS ||
                (topAdsGetShopInfo.value as? Success)?.data.orZero() == SomConsts.TOPADS_AUTO_ADS
    }

    fun setStatusOrderFilter(id: List<Int>) {
        getOrderListParams.statusList = id
        resetNextOrderId()
    }

    fun setSearchParam(keyword: String) {
        getOrderListParams.search = keyword
    }

    fun resetNextOrderId() {
        getOrderListParams.nextOrderId = 0
    }

    fun hasNextPage(): Boolean = getOrderListParams.nextOrderId != 0
}