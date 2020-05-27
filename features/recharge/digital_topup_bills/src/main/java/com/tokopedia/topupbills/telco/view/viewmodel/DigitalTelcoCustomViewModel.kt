package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topupbills.telco.data.TelcoCatalogPrefixSelect
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class DigitalTelcoCustomViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getPrefixOperator(rawQuery: String, menuId: Int,
                          onSuccess: (TelcoCatalogPrefixSelect) -> Unit,
                          onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam[KEY_COMPONENT_ID] = menuId

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogPrefixSelect::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogPrefixSelect>()

            onSuccess(data)
        }) {
            onError(it)
        }
    }

    fun getCustomDataPostpaid(rawQuery: String,
                              onSuccess: (TelcoCatalogPrefixSelect) -> Unit,
                              onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_COMPONENT_ID, TelcoComponentType.CLIENT_NUMBER_POSTPAID)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogPrefixSelect::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogPrefixSelect>()

            onSuccess(data)
        }) {
            onError(it)
        }
    }

    companion object {
        const val KEY_COMPONENT_ID = "menuID"
    }

}