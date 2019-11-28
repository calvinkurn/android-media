package com.tokopedia.digital.productV2.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.productV2.model.DigitalProductData
import com.tokopedia.digital.productV2.model.DigitalProductOperatorCluster
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

class DigitalProductViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val operatorCluster = MutableLiveData<Result<DigitalProductOperatorCluster>>()
    val productList = MutableLiveData<Result<DigitalProductData>>()

    fun getOperatorCluster(rawQuery: String, mapParam: Map<String, Any>, isLoadFromCloud: Boolean = false){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalProductOperatorCluster.Response::class.java, mapParam)
                val graphQlCacheStrategy : GraphqlCacheStrategy
                if(isLoadFromCloud) {
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<DigitalProductOperatorCluster.Response>()
            operatorCluster.value = Success(data.response)
        }){
            operatorCluster.value = Fail(it)
        }
    }

    fun getProductList(rawQuery: String, mapParam: Map<String, Any>, isLoadFromCloud: Boolean = false){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalProductOperatorCluster.Response::class.java, mapParam)
                val graphQlCacheStrategy : GraphqlCacheStrategy
                if(isLoadFromCloud) {
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<DigitalProductOperatorCluster.Response>()
            operatorCluster.value = Success(data.response)
        }){
            operatorCluster.value = Fail(it)
        }
    }

    fun createParams(menuID: Int, operator: String = ""): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params[PARAM_MENU_ID] = menuID
        if (operator.isNotEmpty()) params[PARAM_OPERATOR] = operator
        return params
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }
}
