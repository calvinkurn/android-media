package com.tokopedia.vouchergame.list.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchergame.list.data.VoucherGameListData
import com.tokopedia.vouchergame.list.data.VoucherGameOperator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class VoucherGameListViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    var voucherGameList: List<VoucherGameOperator> = listOf()
    val searchVoucherGameList = MutableLiveData<Result<List<VoucherGameOperator>>>()

    fun getVoucherGameList(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, VoucherGameListData.Response::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<VoucherGameListData.Response>()

            voucherGameList = data.response.operators
            searchVoucherGameList.value = Success(voucherGameList)
        }) {
            searchVoucherGameList.value = Fail(it)
        }
    }

    fun createParams(menuID: Int, platformID: Int): Map<String,Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
//        params.put(PARAM_MENU_ID, menuID)
//        params.put(PARAM_PLATFORM_ID, platformID)
        params.put(PARAM_MENU_ID, 10)
        params.put(PARAM_PLATFORM_ID, 7)
        return params
    }

    fun searchVoucherGame(query: String) {
        searchVoucherGameList.value = Success(voucherGameList.filter { it.attributes.name.contains(query, true) })
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_PLATFORM_ID = "platformID"
    }

}