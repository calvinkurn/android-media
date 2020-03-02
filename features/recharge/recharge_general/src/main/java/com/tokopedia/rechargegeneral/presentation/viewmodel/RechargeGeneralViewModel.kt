package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeGeneralViewModel  @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _operatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val operatorCluster : LiveData<Result<RechargeGeneralOperatorCluster>>
        get() = _operatorCluster

    private val _productList = MutableLiveData<Result<RechargeGeneralProductData>>()
    val productList : LiveData<Result<RechargeGeneralProductData>>
        get() = _productList

    fun getOperatorCluster(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralOperatorCluster.Response::class.java, mapParams)
                val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeGeneralOperatorCluster.Response>()

            _operatorCluster.postValue(Success(data.response))
        }) {
            _operatorCluster.postValue(Fail(it))
        }
    }

    fun getProductList(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralProductData.Response::class.java, mapParams)
                val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeGeneralProductData.Response>()

            _productList.postValue(Success(data.response))
        }) {
            _productList.postValue(Fail(it))
        }
    }

    fun createOperatorClusterParams(menuID: Int): Map<String, Int> {
        return mapOf(PARAM_MENU_ID to menuID)
    }

    fun createProductListParams(menuID: Int, operator: Int): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuID, PARAM_OPERATOR to operator.toString())
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
    }
}
