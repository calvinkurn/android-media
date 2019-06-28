package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topupbills.telco.data.TelcoEnquiryData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class DigitalTelcoEnquiryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getEnquiry(rawQuery: String, mapParam: Map<String, kotlin.Any>,
                   onSuccess: (TelcoEnquiryData) -> Unit,
                   onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoEnquiryData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoEnquiryData>()

            onSuccess(data)
        }) {
            onError(it)
        }
    }
}