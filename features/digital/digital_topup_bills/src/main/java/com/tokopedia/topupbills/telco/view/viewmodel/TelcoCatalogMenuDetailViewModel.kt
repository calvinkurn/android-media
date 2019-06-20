package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.topupbills.telco.data.TelcoCatalogMenuDetailData
import com.tokopedia.topupbills.telco.data.TelcoRechargeFavNumberData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 23/05/19.
 */
class TelcoCatalogMenuDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                          val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getCatalogMenuDetail(rawQuery: String, mapParam: Map<String, kotlin.Any>,
                             onLoading: (Boolean) -> Unit,
                             onSuccess: (TelcoCatalogMenuDetailData) -> Unit,
                             onError: (Throwable) -> Unit) {
        onLoading(true)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            onLoading(false)
            if (data.catalogMenuDetailData != null) {
                onSuccess(data)
            } else {
                onError(ResponseErrorException())
            }
        }) {
            onLoading(false)
            onError(it)
        }
    }

    fun getFavNumbers(rawQuery: String, mapParam: Map<String, kotlin.Any>,
                      onSuccess: (TelcoRechargeFavNumberData) -> Unit,
                      onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoRechargeFavNumberData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoRechargeFavNumberData>()

            if (data.favNumber.favNumberList.isNotEmpty()) {
                onSuccess(data)
            } else {
                onError(ResponseErrorException())
            }
        }) {
            onError(it)
        }
    }

}