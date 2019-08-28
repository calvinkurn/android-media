package com.tokopedia.common.topupbills.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 28/08/19.
 */
class TopupBillsViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val enquiryData = MutableLiveData<Result<TelcoEnquiryData>>()

    fun getEnquiry(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoEnquiryData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoEnquiryData>()

            enquiryData.value = Success(data)
        }) {
            enquiryData.value = Fail(it)
        }
    }

    fun createEnquiryParams(clientNumber: String, productId: String): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params.put(PARAM_CLIENT_NUMBER, clientNumber)
        params.put(PARAM_PRODUCT_ID, productId)
//        params.put(PARAM_CLIENT_NUMBER, 10)
//        params.put(PARAM_PRODUCT_ID, "472")
        return params
    }

    companion object {
        const val PARAM_CLIENT_NUMBER = "clientNumber"
        const val PARAM_PRODUCT_ID = "productId"
    }

}