package com.tokopedia.sellerorder.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.domain.usecase.SomGetUserRoleUseCase
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.list.SomGetFilterListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderStatusListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetTickerListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.Job
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

    private val _filterResult = MutableLiveData<Result<SomListFilter.Data.OrderFilterSom>>()
    val filterResult: LiveData<Result<SomListFilter.Data.OrderFilterSom>>
        get() = _filterResult

    private val _statusOrderListResult = MutableLiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>()
    val statusOrderListResult: LiveData<Result<MutableList<SomListAllFilter.Data.OrderFilterSomSingle.StatusList>>>
        get() = _statusOrderListResult

    private val _orderListResult = MutableLiveData<Result<SomListOrder.Data.OrderList>>()
    val orderListResult: LiveData<Result<SomListOrder.Data.OrderList>>
        get() = _orderListResult

    private val _userRoleResult = MutableLiveData<Result<SomGetUserRoleUiModel>>()
    val userRoleResult: LiveData<Result<SomGetUserRoleUiModel>>
        get() = _userRoleResult

    private var getUserRolesJob: Job? = null

    fun loadTickerList(tickerQuery: String) {
        launchCatchError(block = {
            val requestTickerParams = SomListTickerParam(requestBy = PARAM_SELLER, client = PARAM_CLIENT)
            _tickerListResult.postValue(getTickerListUseCase.execute(requestTickerParams, tickerQuery))
        }, onError = {
            _tickerListResult.postValue(Fail(it))
        })
    }

    fun loadStatusOrderList(rawQuery: String) {
        launchCatchError(block = {
            _statusOrderListResult.postValue(getOrderStatusListUseCase.execute(rawQuery))
        }, onError = {
            _statusOrderListResult.postValue(Fail(it))
        })
    }

    fun loadFilter() {
        launchCatchError(block =  {
            _filterResult.postValue(getFilterListUseCase.execute())
        }, onError = {
            _filterResult.postValue(Fail(it))
        })
    }

    fun loadOrderList(paramOrder: SomListOrderParam) {
        launchCatchError(block =  {
            _orderListResult.postValue(getOrderListUseCase.execute(paramOrder))
        }, onError = {
            _orderListResult.postValue(Fail(it))
        })
    }

    fun loadUserRoles(userId: Int) {
        if (getUserRolesJob == null || getUserRolesJob?.isCompleted != false) {
            getUserRolesJob = launchCatchError(block = {
                getUserRoleUseCase.setUserId(userId)
                _userRoleResult.postValue(getUserRoleUseCase.execute())
            }, onError = {
                _userRoleResult.postValue(Fail(it))
            })
        }
    }

    fun clearUserRoles() {
        _userRoleResult.postValue(null)
    }
}