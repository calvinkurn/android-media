package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.topupbills.telco.data.TelcoRechargeComponentData
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class DigitalTelcoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getRechargeCollections(rawQuery: String, mapParam: Map<String, kotlin.Any>,
                               onSuccess: (TelcoRechargeComponentData) -> Unit,
                               onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoRechargeComponentData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoRechargeComponentData>()

            if (!data.rechargeComponentData.dataCollections.isEmpty()) {
                onSuccess(data)
            } else {
                onError(ResponseErrorException())
            }
        }) {
            onError(it)
        }
    }

}