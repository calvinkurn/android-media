package com.tokopedia.topupbills.telco.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topupbills.telco.data.TelcoCatalogMenuDetailData
import com.tokopedia.topupbills.telco.data.TelcoRechargeFavNumberData
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
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

    fun getCatalogMenuDetailPrepaid(rawQuery: String,
                                    onLoading: (Boolean) -> Unit,
                                    onSuccess: (TelcoCatalogMenuDetailData) -> Unit,
                                    onError: (Throwable) -> Unit) {
        onLoading(true)
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_MENU_ID, TelcoComponentType.TELCO_PREPAID)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            onLoading(false)
            onSuccess(data)
        }) {
            onLoading(false)
            onError(it)
        }
    }

    fun getCatalogMenuDetailPostpaid(rawQuery: String,
                                     onLoading: (Boolean) -> Unit,
                                     onSuccess: (TelcoCatalogMenuDetailData) -> Unit,
                                     onError: (Throwable) -> Unit) {
        onLoading(true)
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_MENU_ID, TelcoComponentType.TELCO_POSTPAID)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            onLoading(false)
            onSuccess(data)
        }) {
            onLoading(false)
            onError(it)
        }
    }

    fun getFavNumbersPostpaid(rawQuery: String,
                              onSuccess: (TelcoRechargeFavNumberData) -> Unit,
                              onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_CATEGORY_ID, TelcoComponentType.FAV_NUMBER_POSTPAID)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoRechargeFavNumberData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoRechargeFavNumberData>()

            onSuccess(data)
        }) {
            onError(it)
        }
    }

    fun getFavNumbersPrepaid(rawQuery: String,
                             onSuccess: (TelcoRechargeFavNumberData) -> Unit,
                             onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, Any>()
            mapParam.put(KEY_CATEGORY_ID, TelcoComponentType.FAV_NUMBER_PREPAID)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoRechargeFavNumberData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoRechargeFavNumberData>()

            onSuccess(data)
        }) {
            onError(it)
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val KEY_CATEGORY_ID = "categoryID"
    }

}