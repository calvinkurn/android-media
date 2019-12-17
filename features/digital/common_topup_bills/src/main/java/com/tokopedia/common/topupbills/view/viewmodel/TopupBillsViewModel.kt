package com.tokopedia.common.topupbills.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 28/08/19.
 */
class TopupBillsViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val enquiryData = MutableLiveData<Result<TopupBillsEnquiryData>>()
    val menuDetailData = MutableLiveData<Result<TopupBillsMenuDetail>>()
    val favNumberData = MutableLiveData<Result<TopupBillsFavNumber>>()

    fun getEnquiry(rawQuery: String, mapParam: List<TopupBillsEnquiryQuery>) {
        val params = mapOf(PARAM_FIELDS to mapParam)
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, TopupBillsEnquiryData::class.java, params)
            var data: TopupBillsEnquiryData
            do {
                data = withContext(Dispatchers.Default) {
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                }.getSuccessData()

                // If data is pending delay query call
                if (data.enquiry.status == STATUS_PENDING) delay((data.enquiry.retryDuration?.toLong() ?: 1) * 1000)
            } while (data.enquiry.status != STATUS_DONE)
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

    fun getFavoriteNumbers(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TopupBillsFavNumberData::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TopupBillsFavNumberData>()

            favNumberData.value = Success(data.favNumber)
        }) {
            favNumberData.value = Fail(it)
        }
    }

    fun createEnquiryParams(operatorId: String, productId: String, inputData: Array<Map<String, String>>): List<TopupBillsEnquiryQuery> {
        val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
        // Hardcode source type field, temporary?
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_SOURCE_TYPE, ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_DEVICE_ID, ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE))
//        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_OPERATOR_ID, operatorId))
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_PRODUCT_ID, productId))
        inputData.forEach {
            it.forEach { (key, value) ->
                enquiryParams.add(TopupBillsEnquiryQuery(key, value))
            }
        }
        return enquiryParams
    }

    fun createMenuDetailParams(menuId: Int): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuId)
    }

    fun createFavoriteNumbersParams(categoryId: Int): Map<String, Any> {
        return mapOf(PARAM_CATEGORY_ID to categoryId)
    }

    companion object {
        const val PARAM_FIELDS = "fields"

        const val PARAM_MENU_ID = "menuID"
        const val PARAM_CATEGORY_ID = "categoryID"

        const val STATUS_DONE = "DONE"
        const val STATUS_PENDING = "PENDING"

        const val ENQUIRY_PARAM_OPERATOR_ID = "operator_id"
        const val ENQUIRY_PARAM_PRODUCT_ID = "product_id"
        const val ENQUIRY_PARAM_DEVICE_ID = "device_id"
        const val ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE = "4"
        const val ENQUIRY_PARAM_SOURCE_TYPE = "source_type"
        const val ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE = "c20ad4d76fe977"
    }

}