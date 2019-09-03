package com.tokopedia.sellerorder.list.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CLIENT
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
    // val dummyStatusList = ArrayList<Int>()

    fun loadSomListData(tickerQuery: String, filterQuery: String) {
        // dummyStatusList.add(220)
        launch {
            getTickerList(tickerQuery)
            getFilterList(filterQuery)
        }
    }

    fun loadOrderList(orderQuery: String, statusList: List<Int>) {
        launch {
            getOrderList(orderQuery, statusList)
        }
    }

    suspend fun getTickerList(rawQuery: String) {
        val requestTickerParams = SomListTickerParam(requestBy = PARAM_SELLER, client = PARAM_CLIENT)
        val tickerParams = mapOf(PARAM_INPUT to requestTickerParams)
        try {
            val tickerListData = async {
                val response = withContext(Dispatchers.Default) {
                    val tickerRequest = GraphqlRequest(rawQuery, POJO_TICKER, tickerParams)
                    graphqlRepository.getReseponse(listOf(tickerRequest))
                            .getSuccessData<SomListTicker.Data>()
                }
                response
            }

            tickerListResult.value = Success(tickerListData.await().orderTickers.listTicker.toMutableList())
        } catch (t: Throwable) {
            tickerListResult.value = Fail(t)
        }
    }

    suspend fun getFilterList(rawQuery: String) {
        try {
            val filterListData = async {
                val response = withContext(Dispatchers.Default) {
                    val filterRequest = GraphqlRequest(rawQuery, POJO_FILTER)
                    graphqlRepository.getReseponse(listOf(filterRequest))
                            .getSuccessData<SomListFilter.Data>()
                }
                response
            }

            val statusListOrder = filterListData.await().orderFilterSom.statusList.toMutableList()
            filterListResult.value = Success(statusListOrder)
        } catch (t: Throwable) {
            filterListResult.value = Fail(t)
        }
    }

    suspend fun getOrderList(rawQuery: String, statusList: List<Int>) {
        val requestOrderParams = SomListOrderParam("", "", "", FILTER_STATUS, statusList, SORT_BY)
        val orderParams = mapOf(PARAM_INPUT to requestOrderParams)
        try {
            val orderListData = async {
                val response = withContext(Dispatchers.Default) {
                    val orderRequest = GraphqlRequest(rawQuery, POJO_ORDER, orderParams)
                    graphqlRepository.getReseponse(listOf(orderRequest))
                            .getSuccessData<SomListOrder.Data>()
                }
                response
            }

            orderListResult.value = Success(orderListData.await().orderList.orders.toMutableList())
        } catch (t: Throwable) {
            orderListResult.value = Fail(t)
        }
    }

    /*private suspend fun getTickerListAsync(rawQuery: String, requestBy: String, client: String, fromCloud: Boolean) {
            tickerListResult.value = tickerUseCase.execute(rawQuery, requestBy, client, fromCloud)
        }
    }

    fun getFilters(rawQuery: String, fromCloud: Boolean) = launch {
            filterListResult.value = somListFilterUseCase.execute(rawQuery, fromCloud)
    }

    fun getOrdersAsync(rawQuery: String, statusList: List<Int>, fromCloud: Boolean) = async(start = CoroutineStart.LAZY) {
        orderListResult.value = somListOrderUseCase.execute(rawQuery, statusList, fromCloud)
    }*/

    companion object {
        const val PARAM_INPUT = "input"

        private val POJO_TICKER = SomListTicker.Data::class.java
        private val POJO_FILTER = SomListFilter.Data::class.java
        private val POJO_ORDER = SomListOrder.Data::class.java

        private val FILTER_STATUS = 999
        private val SORT_BY = 2
    }
}