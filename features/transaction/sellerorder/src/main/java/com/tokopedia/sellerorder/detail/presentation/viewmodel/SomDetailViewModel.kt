package com.tokopedia.sellerorder.detail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
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

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launch { getDetailOrder(detailQuery, orderId) }
    }

    suspend fun getDetailOrder(rawQuery: String, orderId: String) {
        val requestDetailParams: HashMap<String, String> = hashMapOf(VAR_PARAM_ORDERID to orderId,
                VAR_PARAM_LANG to PARAM_LANG_ID)
        // val requestDetailParams = mapOf("" to detailParam)
        try {
            val orderDetailData = async {
                val response = withContext(Dispatchers.Default) {
                    val detailRequest = GraphqlRequest(rawQuery, POJO_DETAIL, requestDetailParams as Map<String, Any>?)
                    graphqlRepository.getReseponse(listOf(detailRequest))
                            .getSuccessData<SomDetailOrder.Data>()
                }
                response
            }

            orderDetailResult.value = Success(orderDetailData.await().getSomDetail)
        } catch (t: Throwable) {
            orderDetailResult.value = Fail(t)
        }
    }

    companion object {
        private val POJO_DETAIL = SomDetailOrder.Data::class.java
    }
}