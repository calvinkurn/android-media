package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topupbills.telco.data.TelcoProductComponentData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class DigitalTelcoProductViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    // cache in 10 minutes
    fun getProductCollections(rawQuery: String, mapParam: Map<String, Any>, productType: Int,
                              onLoading: (Boolean) -> Unit,
                              onSuccess: (TelcoProductComponentData) -> Unit,
                              onError: (Throwable) -> Unit) {
        onLoading(true)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoProductComponentData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<TelcoProductComponentData>()

            onLoading(false)
            data.productType = productType
            onSuccess(data)
        }) {
            onLoading(false)
            onError(it)
        }
    }

}