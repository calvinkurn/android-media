package com.tokopedia.sellerorder.detail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_IS_FROM_FINTECH
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
import com.tokopedia.sellerorder.detail.data.model.SomAcceptOrder
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                            private val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {

    val orderDetailResult = MutableLiveData<Result<SomDetailOrder.Data.GetSomDetail>>()
    val acceptOrderResult = MutableLiveData<Result<SomAcceptOrder.Data>>()

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launch { getDetailOrder(detailQuery, orderId) }
    }

    fun acceptOrder(acceptOrderQuery: String, orderId: String, shopId: String) {
        launch { doAcceptOrder(acceptOrderQuery, orderId, shopId) }
    }

    suspend fun getDetailOrder(rawQuery: String, orderId: String) {
        val requestDetailParams: HashMap<String, String> = hashMapOf(VAR_PARAM_ORDERID to orderId,
                VAR_PARAM_LANG to PARAM_LANG_ID)
        launchCatchError(block = {
            val orderDetailData = withContext(Dispatchers.IO) {
                val detailRequest = GraphqlRequest(rawQuery, POJO_DETAIL, requestDetailParams as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(detailRequest))
                        .getSuccessData<SomDetailOrder.Data>()
            }
            orderDetailResult.postValue(Success(orderDetailData.getSomDetail))
        }, onError = {
            orderDetailResult.postValue(Fail(it))
        })
    }

    suspend fun doAcceptOrder(rawQuery: String, orderId: String, shopId: String) {
        val requestAcceptOrderParam = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_SHOP_ID to shopId, PARAM_IS_FROM_FINTECH to false)
        val acceptOrderParams = mapOf(SomConsts.PARAM_INPUT to requestAcceptOrderParam)
        launchCatchError(block = {
            val acceptOrderData = withContext(Dispatchers.IO) {
                val acceptOrderRequest = GraphqlRequest(rawQuery, POJO_ACCEPT_ORDER, acceptOrderParams as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(acceptOrderRequest))
                        .getSuccessData<SomAcceptOrder.Data>()
            }
            acceptOrderResult.postValue(Success(acceptOrderData))
        }, onError = {
            acceptOrderResult.postValue(Fail(it))
        })
    }

    companion object {
        private val POJO_DETAIL = SomDetailOrder.Data::class.java
        private val POJO_ACCEPT_ORDER = SomAcceptOrder.Data::class.java
    }
}