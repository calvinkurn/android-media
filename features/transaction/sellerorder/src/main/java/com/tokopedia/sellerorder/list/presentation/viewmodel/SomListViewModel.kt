package com.tokopedia.sellerorder.list.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SELLER
import com.tokopedia.sellerorder.list.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-27.
 */
class SomListViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                           private val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {

    val tickerListResult = MutableLiveData<Result<MutableList<SomListTicker.Data.OrderTickers.Tickers>>>()
    val filterListResult = MutableLiveData<Result<MutableList<SomListFilter.Data.OrderFilterSom.StatusList>>>()
    val orderListResult = MutableLiveData<Result<MutableList<SomListOrder.Data.OrderList.Order>>>()

    fun loadSomListData(tickerQuery: String, filterQuery: String) {
        launch { getTickerList(tickerQuery) }
        launch { getFilterList(filterQuery) }
    }

    fun loadOrderList(orderQuery: String, paramOrder: SomListOrderParam) {
        launch { getOrderList(orderQuery, paramOrder) }
    }

    suspend fun getTickerList(rawQuery: String) {
        val requestTickerParams = SomListTickerParam(requestBy = PARAM_SELLER, client = PARAM_CLIENT)
        val tickerParams = mapOf(PARAM_INPUT to requestTickerParams)
        launchCatchError(block = {
            val tickerListData = withContext(Dispatchers.IO) {
                val tickerRequest = GraphqlRequest(rawQuery, POJO_TICKER, tickerParams as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(tickerRequest))
                        .getSuccessData<SomListTicker.Data>()
                }
            tickerListResult.value = Success(tickerListData.orderTickers.listTicker.toMutableList())
        }, onError = {
            tickerListResult.postValue(Fail(it))
        })
    }

    suspend fun getFilterList(rawQuery: String) {
        launchCatchError(block = {
            val filterListData = withContext(Dispatchers.IO) {
                val filterRequest = GraphqlRequest(rawQuery, POJO_FILTER)
                graphqlRepository.getReseponse(listOf(filterRequest))
                        .getSuccessData<SomListFilter.Data>()
            }
            filterListResult.postValue(Success(filterListData.orderFilterSom.statusList.toMutableList()))
        }, onError = {
            filterListResult.postValue(Fail(it))
        })
    }

    suspend fun getOrderList(rawQuery: String, requestOrderParams: SomListOrderParam) {
        val orderParams = mapOf(PARAM_INPUT to requestOrderParams)
        launchCatchError(block = {
            val orderListData = withContext(Dispatchers.IO) {
                val orderRequest = GraphqlRequest(rawQuery, POJO_ORDER, orderParams)
                graphqlRepository.getReseponse(listOf(orderRequest))
                        .getSuccessData<SomListOrder.Data>()
            }
            orderListResult.postValue(Success(orderListData.orderList.orders.toMutableList()))
        }, onError = {
            orderListResult.postValue(Fail(it))
        })
    }

    companion object {
        private val POJO_TICKER = SomListTicker.Data::class.java
        private val POJO_FILTER = SomListFilter.Data::class.java
        private val POJO_ORDER = SomListOrder.Data::class.java
    }
}