package com.tokopedia.sellerorder.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.sellerorder.list.domain.list.SomGetFilterListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetOrderStatusListUseCase
import com.tokopedia.sellerorder.list.domain.list.SomGetTickerListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class SomListViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                           private val getTickerListUseCase: SomGetTickerListUseCase,
                                           private val getOrderStatusListUseCase: SomGetOrderStatusListUseCase,
                                           private val getFilterListUseCase: SomGetFilterListUseCase,
                                           private val getOrderListUseCase: SomGetOrderListUseCase) : BaseViewModel(dispatcher.ui()) {

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

    fun loadFilterList(filterQuery: String) {
        launchCatchError(block =  {
            _filterListResult.postValue(getFilterListUseCase.execute(filterQuery))
        }, onError = {
            _filterListResult.postValue(Fail(it))
        })
    }

    fun loadOrderList(orderQuery: String, paramOrder: SomListOrderParam) {
        launchCatchError(block =  {
            _orderListResult.postValue(getOrderListUseCase.execute(paramOrder, orderQuery))
        }, onError = {
            _orderListResult.postValue(Fail(it))
        })
    }
}