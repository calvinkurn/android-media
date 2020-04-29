package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.*
import com.tokopedia.smartbills.data.api.SmartBillsRepository
import com.tokopedia.smartbills.util.SmartBillsDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST
            ).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeStatementMonths.Response>()

            if (data.response.isNotEmpty()) {
                mutableStatementMonths.postValue(Success(data.response))
            } else {
                mutableStatementMonths.postValue(
                    Fail(MessageErrorException("Terdapat kesalahan pada pengambilan data"))
                )
            }
        }) {
            mutableStatementMonths.postValue(Fail(it))
        }
    }

    fun getStatementBills(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeStatementBills.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST
            ).build()
            val data = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeStatementBills.Response>()

            mutableStatementBills.postValue(Success(data.response))
        }) {
            mutableStatementBills.postValue(Fail(it))
        }
    }

    fun runMultiCheckout(request: MultiCheckoutRequest?) {
        if (request != null) {
            launchCatchError(block = {
                val data = withContext(dispatcher.IO) {
                    smartBillsRepository.postMultiCheckout(request)
                }

                mutableMultiCheckout.postValue(Success(data))
            }) {
                mutableMultiCheckout.postValue(Fail(it))
            }
        } else {
            mutableMultiCheckout.postValue(
                Fail(MessageErrorException("Terjadi kesalahan pada pemrosesan data"))
            )
        }
    }

    fun createStatementMonthsParams(limit: Int): Map<String, Int> {
        return mapOf(PARAM_LIMIT to limit)
    }

    fun createStatementBillsParams(month: Int, year: Int): Map<String, Int> {
        return mapOf(PARAM_MONTH to month, PARAM_YEAR to year)
    }

    fun createMultiCheckoutParams(bills: List<RechargeBills>, userSession: UserSessionInterface): MultiCheckoutRequest? {
        val validBills = bills.filter { it.index >= 0 }
        return if (validBills.isNotEmpty()) {
            val requestData = validBills.map {
                MultiCheckoutRequest.MultiCheckoutRequestItem(it.index, it.productID, it.checkoutFields, "")
            }

            val requestBodyIdentifier = RequestBodyIdentifier()
            requestBodyIdentifier.deviceToken = userSession.deviceId
            requestBodyIdentifier.userId = userSession.userId
            requestBodyIdentifier.osType = DEFAULT_OS_TYPE

            MultiCheckoutRequest(
                    MultiCheckoutRequest.MultiCheckoutRequestAttributes(requestBodyIdentifier, requestData)
            )
        } else null
    }

    companion object {
        const val PARAM_LIMIT = "limit"
        const val PARAM_MONTH = "month"
        const val PARAM_YEAR = "year"

        const val DEFAULT_OS_TYPE = "1"
    }
}