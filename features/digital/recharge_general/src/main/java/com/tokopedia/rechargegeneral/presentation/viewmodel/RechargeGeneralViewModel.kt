package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.rechargegeneral.domain.GetProductUseCase
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeGeneralViewModel  @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val operatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val productList = MutableLiveData<Result<RechargeGeneralProductData>>()

    lateinit var operatorClusterQuery: String
    lateinit var productListQuery: String

    fun getOperatorCluster(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        if (::operatorClusterQuery.isInitialized) {
            launchCatchError(block = {
                val data = withContext(Dispatchers.Default) {
                    val graphqlRequest = GraphqlRequest(operatorClusterQuery, RechargeGeneralOperatorCluster.Response::class.java, mapParams)
                    val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
                    graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
                }.getSuccessData<RechargeGeneralOperatorCluster.Response>()

                operatorCluster.value = Success(data.response)
            }) {
                operatorCluster.value = Fail(it)
            }
        }
    }

    fun getProductList(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        if (::productListQuery.isInitialized) {
            launchCatchError(block = {
                val data = withContext(Dispatchers.Default) {
                    val graphqlRequest = GraphqlRequest(productListQuery, RechargeGeneralProductData.Response::class.java, mapParams)
                    val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
                    graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
                }.getSuccessData<RechargeGeneralProductData.Response>()

                productList.value = Success(data.response)
            }) {
                productList.value = Fail(it)
            }
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
