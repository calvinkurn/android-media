package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.rechargegeneral.model.RechargeGeneralData
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeGeneralViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val operatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val productList = MutableLiveData<Result<RechargeGeneralData>>()

    fun getOperatorCluster(rawQuery: String, mapParam: Map<String, Any>, isLoadFromCloud: Boolean = false){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralOperatorCluster.Response::class.java, mapParam)
                val graphQlCacheStrategy = if(isLoadFromCloud) {
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<RechargeGeneralOperatorCluster.Response>()
            operatorCluster.value = Success(data.response)
        }){
            operatorCluster.value = Fail(it)
        }
    }

    fun getProductList(rawQuery: String, mapParam: Map<String, Any>, isLoadFromCloud: Boolean = false){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralData.Response::class.java, mapParam)
                val graphQlCacheStrategy = if(isLoadFromCloud) {
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<RechargeGeneralData.Response>()
            productList.value = Success(data.response)
        }){
            val error = it
            productList.value = Fail(it)
        }
    }

    fun createParams(menuID: Int, operator: Int? = null): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        operator?.let { opr -> params[PARAM_OPERATOR] = opr.toString() }
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }
}
