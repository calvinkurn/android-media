package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import com.tokopedia.smartbills.data.RechargeStatementBills
import com.tokopedia.smartbills.data.RechargeStatementMonths
import com.tokopedia.smartbills.data.api.SmartBillsRepository
import com.tokopedia.smartbills.util.SmartBillsDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SmartBillsViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val smartBillsRepository: SmartBillsRepository,
        private val dispatcher: SmartBillsDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    private val mutableStatementMonths = MutableLiveData<Result<List<RechargeStatementMonths>>>()
    val statementMonths: LiveData<Result<List<RechargeStatementMonths>>>
        get() = mutableStatementMonths

    private val mutableStatementBills = MutableLiveData<Result<RechargeStatementBills>>()
    val statementBills: LiveData<Result<RechargeStatementBills>>
        get() = mutableStatementBills

    private val mutableMultiCheckout = MutableLiveData<Result<RechargeMultiCheckoutResponse>>()
    val multiCheckout: LiveData<Result<RechargeMultiCheckoutResponse>>
        get() = mutableMultiCheckout

    fun getStatementMonths(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeStatementMonths.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeStatementMonths.Response>()

            mutableStatementMonths.postValue(Success(data.response))
        }) {
            mutableStatementMonths.postValue(Fail(it))
        }
    }

    fun getStatementBills(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeStatementBills.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeStatementBills.Response>()

            mutableStatementBills.postValue(Success(data.response))
        }) {
            mutableStatementBills.postValue(Fail(it))
        }
    }

    fun runMultiCheckout(mapParams: Map<String, Any>, userId: String) {
        val idempotencyKey = "" //TODO: Set idempotency key
        launchCatchError(block = {
            val data = withContext(dispatcher.IO) {
                smartBillsRepository.postMultiCheckout(mapParams, userId, idempotencyKey)
            }

            mutableMultiCheckout.postValue(Success(data))
        }) {
            mutableMultiCheckout.postValue(Fail(it))
        }
    }

    fun createStatementMonthsParams(limit: Int): Map<String, Int> {
        return mapOf(PARAM_LIMIT to limit)
    }

    fun createStatementBillsParams(month: Int, year: Int): Map<String, Int> {
        return mapOf(PARAM_MONTH to month, PARAM_YEAR to year)
    }

    fun createMultiCheckoutParams(items: List<RechargeBills>): Map<String, Any> {
        val itemParams = items.mapIndexed { index, item ->
            val itemMap = mutableMapOf<String, Any>()
            itemMap[PARAM_INDEX] = index
            itemMap[PARAM_PRODUCT_ID] = item.productID

            val fieldParams = item.checkoutFields.map { it.getMap() }
            itemMap[PARAM_FIELDS] = fieldParams
            return itemMap
        }
        return mapOf<String, Any>(PARAM_DATA to mapOf(PARAM_ATTRIBUTES to mapOf(PARAM_ITEMS to itemParams)))
    }

    companion object {
        const val PARAM_LIMIT = "limit"
        const val PARAM_MONTH = "month"
        const val PARAM_YEAR = "year"

        const val PARAM_INDEX = "index"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_FIELDS = "fields"
        const val PARAM_CART_UUID = "cart_uuid"
        const val PARAM_DATA = "data"
        const val PARAM_ATTRIBUTES = "attributes"
        const val PARAM_ITEMS = "items"
    }
}