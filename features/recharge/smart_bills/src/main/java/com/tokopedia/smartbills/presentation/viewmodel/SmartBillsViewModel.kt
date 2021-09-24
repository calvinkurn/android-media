package com.tokopedia.smartbills.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.*
import com.tokopedia.smartbills.usecase.SmartBillsMultiCheckoutUseCase
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.mapActiontoStatement
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class SmartBillsViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val smartBillsMultiCheckoutUseCase: SmartBillsMultiCheckoutUseCase,
        private val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.io) {

    private val mutableStatementMonths = MutableLiveData<Result<List<RechargeStatementMonths>>>()
    val statementMonths: LiveData<Result<List<RechargeStatementMonths>>>
        get() = mutableStatementMonths

    private val mutableStatementBills = MutableLiveData<Result<RechargeListSmartBills>>()
    val statementBills: LiveData<Result<RechargeListSmartBills>>
        get() = mutableStatementBills

    private val mutableMultiCheckout = MutableLiveData<Result<RechargeMultiCheckoutResponse>>()
    val multiCheckout: LiveData<Result<RechargeMultiCheckoutResponse>>
        get() = mutableMultiCheckout

    fun getStatementMonths(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.STATEMENT_MONTHS_QUERY,
                    RechargeStatementMonths.Response::class.java, mapParams
            )
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST
            ).setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeStatementMonths.Response>()

            if (!data.response.isNullOrEmpty()) {
                mutableStatementMonths.postValue(Success(data.response))
            } else {
                throw(MessageErrorException(STATEMENT_MONTHS_ERROR))
            }
        }) {
            mutableStatementMonths.postValue(Fail(it))
        }
    }

    fun getStatementBills(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.STATEMENT_BILLS_QUERY_CLUSTERING,
                    RechargeListSmartBills.Response::class.java, mapParams
            )
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST
            ).setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val response = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)
            }

            val error = response.getError(RechargeListSmartBills.Response::class.java)
            if(error.isNullOrEmpty()){
                val data = response.getSuccessData<RechargeListSmartBills.Response>()
                if (data.response != null) {
                    mutableStatementBills.postValue(Success(data.response))
                } else {
                    throw(MessageErrorException(STATEMENT_BILLS_ERROR))
                }
            } else {
                val firstError = error.firstOrNull()
                if (firstError?.extensions?.developerMessage?.contains(HARD_CODE_EMPTY_RESPONSE) ?: false){
                    mutableStatementBills.postValue(Success(RechargeListSmartBills()))
                } else {
                    throw(MessageErrorException(firstError?.message))
                }
            }
        }) {
            mutableStatementBills.postValue(Fail(it))
        }
    }

    fun getSBMWithAction(mapParams: Map<String, Any>, rechargeListSmartBills: RechargeListSmartBills){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.GET_SBM_RELOAD_ACTION_QUERY,
                    RechargeMultipleSBMBill.Response::class.java, mapParams
            )

            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeMultipleSBMBill.Response>()

            if (data.response != null) {
                mutableStatementBills.postValue(Success(mapActiontoStatement(data.response, rechargeListSmartBills)))
            } else {
                throw(MessageErrorException(STATEMENT_BILLS_ERROR))
            }
        }){
            mutableStatementBills.postValue(Fail(it))
        }
    }

    fun runMultiCheckout(request: MultiCheckoutRequest?, userId: String) {
        if (request != null) {
            val idempotencyKey = userId.generateRechargeCheckoutToken()
            val mapParam: HashMap<String, String> = hashMapOf()
            mapParam[IDEMPOTENCY_KEY] = idempotencyKey
            mapParam[CONTENT_TYPE] = "application/json"

            smartBillsMultiCheckoutUseCase.setParam(request)
            smartBillsMultiCheckoutUseCase.setHeader(mapParam)

            launchCatchError(block = {
                val data = withContext(dispatcher.io) {
                    convertSBMMultiResponse(smartBillsMultiCheckoutUseCase.executeOnBackground()).data
                }

                mutableMultiCheckout.postValue(Success(data))
            }) {
                mutableMultiCheckout.postValue(Fail(it))
            }
        } else {
            mutableMultiCheckout.postValue(
                Fail(MessageErrorException(MULTI_CHECKOUT_EMPTY_REQUEST))
            )
        }
    }

    fun createStatementMonthsParams(limit: Int): Map<String, Int> {
        return mapOf(PARAM_LIMIT to limit)
    }

    fun createStatementBillsParams(month: Int, year: Int, source: Int? = null): Map<String, Int> {
        val map = mutableMapOf(PARAM_MONTH to month, PARAM_YEAR to year)
        source?.run { map[PARAM_SOURCE] = source }
        return map
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

    fun createRefreshActionParams(uuids:List<String>, month: Int, year: Int, source: Int? = null): Map<String, Any> {
        val map = mutableMapOf(PARAM_UUIDS to uuids, PARAM_MONTH to month, PARAM_YEAR to year)
        source?.run { map[PARAM_SOURCE] = source }
        return map
    }

    fun convertSBMMultiResponse(typeRestResponseMap: Map<Type, RestResponse?>): DataRechargeMultiCheckoutResponse {
        return typeRestResponseMap[DataRechargeMultiCheckoutResponse::class.java]?.getData() as DataRechargeMultiCheckoutResponse
    }

    companion object {
        const val PARAM_LIMIT = "limit"
        const val PARAM_MONTH = "month"
        const val PARAM_YEAR = "year"
        const val PARAM_SOURCE = "source"
        const val PARAM_UUIDS = "uuids"

        const val STATEMENT_MONTHS_ERROR = "STATEMENT_MONTHS_ERROR"
        const val STATEMENT_BILLS_ERROR = "STATEMENT_BILLS_ERROR"
        const val MULTI_CHECKOUT_EMPTY_REQUEST = "MULTI_CHECKOUT_EMPTY_REQUEST"

        const val DEFAULT_OS_TYPE = "1"
        const val IDEMPOTENCY_KEY = "Idempotency-Key"
        const val CONTENT_TYPE = "Content-Type"

        const val HARD_CODE_EMPTY_RESPONSE = "error get base data: [favorite] empty favorite data"
    }
}