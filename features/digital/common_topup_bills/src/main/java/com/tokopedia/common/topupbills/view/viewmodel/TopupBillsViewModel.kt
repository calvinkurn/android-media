package com.tokopedia.common.topupbills.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
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
    val menuDetailData = MutableLiveData<Result<TopupBillsMenuDetail>>()

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

    fun getMenuDetail(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoCatalogMenuDetailData>()

            menuDetailData.value = Success(data.catalogMenuDetailData)
        }) {
            menuDetailData.value = Fail(it)
        }
    }

    fun createEnquiryParams(clientNumber: String, productId: String): Map<String, Any> {
        val params: MutableMap<String, Any> = mutableMapOf()
        params.put(PARAM_CLIENT_NUMBER, clientNumber)
        params.put(PARAM_PRODUCT_ID, productId)
        return params
    }

    fun createMenuDetailParams(menuId: Int): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuId)
    }

    companion object {
        const val PARAM_CLIENT_NUMBER = "clientNumber"
        const val PARAM_PRODUCT_ID = "productId"

        const val PARAM_MENU_ID = "menuID"
    }

}