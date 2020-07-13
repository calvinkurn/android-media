package com.tokopedia.sellerorder.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleDataModel
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.list.SomGetFilterListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderStatusListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetTickerListUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class SomListViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                           private val getTickerListUseCase: SomGetTickerListUseCase,
                                           private val getOrderStatusListUseCase: SomGetOrderStatusListUseCase,
                                           private val getFilterListUseCase: SomGetFilterListUseCase,
                                           private val getOrderListUseCase: SomGetOrderListUseCase,
                                           private val getUserRoleUseCase: SomGetUserRoleUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _tickerListResult = MutableLiveData<Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>>>()
    val tickerListResult: LiveData<Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>>>
        get() = _tickerListResult

    private val _filterListResult = MutableLiveData<Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>>()
    val filterListResult: LiveData<Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>>
        get() = _filterListResult

    private val _statusOrderListResult = MutableLiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>()
    val statusOrderListResult: LiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>
        get() = _statusOrderListResult

    private val _orderListResult = MutableLiveData<Result<SomListOrder.Data.OrderList>>()
    val orderListResult: LiveData<Result<SomListOrder.Data.OrderList>>
        get() = _orderListResult

    private val _userRoleResult = MutableLiveData<Result<SomGetUserRoleDataModel>>()
    val userRoleResult: LiveData<Result<SomGetUserRoleDataModel>>
        get() = _userRoleResult

    fun loadTickerList(tickerQuery: String) {
        val requestTickerParams = SomListTickerParam(requestBy = PARAM_SELLER, client = PARAM_CLIENT)
        launch {
            _tickerListResult.postValue(getTickerListUseCase.execute(requestTickerParams, tickerQuery))
        }
    }

    fun loadStatusOrderList(rawQuery: String) {
        launch {
            _statusOrderListResult.postValue(getOrderStatusListUseCase.execute(rawQuery))
        }
    }

    fun loadFilterList(filterQuery: String) {
        launch {
            _filterListResult.postValue(getFilterListUseCase.execute(filterQuery))
        }
    }

    fun loadOrderList(orderQuery: String, paramOrder: SomListOrderParam) {
        launch {
            _orderListResult.postValue(getOrderListUseCase.execute(paramOrder, orderQuery))
        }
    }

    fun loadUserRoles(userId: Int) {
        launch {
            getUserRoleUseCase.setUserId(userId)
            _userRoleResult.postValue(getUserRoleUseCase.execute())
        }
    }
}