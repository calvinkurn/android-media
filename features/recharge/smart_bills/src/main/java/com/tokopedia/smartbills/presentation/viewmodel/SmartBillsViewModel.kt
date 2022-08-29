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
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.smartbills.data.DataRechargeMultiCheckoutResponse
import com.tokopedia.smartbills.data.MultiCheckoutRequest
import com.tokopedia.smartbills.data.RechargeBills
import com.tokopedia.smartbills.data.RechargeCatalogMenuAddBills
import com.tokopedia.smartbills.data.RechargeCloseParams
import com.tokopedia.smartbills.data.RechargeCloseResponse
import com.tokopedia.smartbills.data.RechargeDeleteSBM
import com.tokopedia.smartbills.data.RechargeListSmartBills
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse
import com.tokopedia.smartbills.data.RechargeMultipleSBMBill
import com.tokopedia.smartbills.data.RechargeRecommendationData
import com.tokopedia.smartbills.data.RechargeRecommendationResponse
import com.tokopedia.smartbills.data.RechargeSBMDeleteBillRequest
import com.tokopedia.smartbills.data.RechargeStatementMonths
import com.tokopedia.smartbills.data.SmartBillsCatalogMenu
import com.tokopedia.smartbills.data.uimodel.HighlightCategoryUiModel
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

    private val mutableCatalogList = MutableLiveData<Result<List<SmartBillsCatalogMenu>>>()
    val catalogList: LiveData<Result<List<SmartBillsCatalogMenu>>>
        get() = mutableCatalogList

    private val mutableDeleteSBM = MutableLiveData<Result<RechargeDeleteSBM>>()
    val deleteSBM: LiveData<Result<RechargeDeleteSBM>>
        get() = mutableDeleteSBM

    private val mutableHighlightCategory = MutableLiveData<Result<HighlightCategoryUiModel>>()
    val highlightCategory: LiveData<Result<HighlightCategoryUiModel>>
        get() = mutableHighlightCategory

    private val mutableRecommendationClose = MutableLiveData<Result<RechargeCloseResponse>>()
    val recommendationClose: LiveData<Result<RechargeCloseResponse>>
        get() = mutableRecommendationClose

    fun getStatementMonths(mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.STATEMENT_MONTHS_QUERY,
                    RechargeStatementMonths.Response::class.java, mapParams
            )
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(
                    if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST
            ).setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_DURATION_MINUTES).build()
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

    fun getStatementBills(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.STATEMENT_BILLS_QUERY_CLUSTERING,
                    RechargeListSmartBills.Response::class.java, mapParams
            )
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
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

    fun getSBMWithAction(mapParams: Map<String, Any>, rechargeListSmartBills: RechargeListSmartBills) {
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
        }) {
            mutableStatementBills.postValue(Fail(it))
        }
    }

    fun getCatalogAddBills(mapParams: Map<String, Any>) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.GET_CATALOG_ADD_BILLS,
                    RechargeCatalogMenuAddBills::class.java, mapParams
            )

            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeCatalogMenuAddBills>()

            mutableCatalogList.postValue(Success(data.response))
          }
        ) {
            mutableCatalogList.postValue(Fail(it))
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

    fun deleteProductSBM(mapParams: Map<String, Any>){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    SmartBillsQueries.DELETE_SBM,
                    RechargeDeleteSBM::class.java, mapParams
            )

            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeDeleteSBM>()

            mutableDeleteSBM.postValue(Success(data))
        }
        ) {
            mutableDeleteSBM.postValue(Fail(it))
        }
    }

    fun getHightlightCategory() {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                SmartBillsQueries.RECHARGE_RECOMMENDATION,
                RechargeRecommendationResponse::class.java, createParamHighlightCategory()
            )

            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeRecommendationResponse>()

            mutableHighlightCategory.postValue(Success(mapperHighlightUiModel(data.response.UUID, data.response.recommendations)))
        }
        ) {
            mutableHighlightCategory.postValue(Fail(it))
        }
    }

    fun closeHighlight(mapParams: Map<String, Any>){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                SmartBillsQueries.CLOSE_RECHARGE_RECOMMENDATION,
                RechargeCloseResponse::class.java, mapParams
            )

            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
            }.getSuccessData<RechargeCloseResponse>()

            mutableRecommendationClose.postValue(Success(data))
        }
        ) {
            mutableRecommendationClose.postValue(Fail(it))
        }
    }

    fun createStatementMonthsParams(limit: Int): Map<String, Int> {
        return mapOf(PARAM_LIMIT to limit)
    }

    fun createCatalogIDParam(platformID: Int): Map<String, Int> {
        return mapOf(PARAM_PLATFORM_ID to platformID)
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
                MultiCheckoutRequest.MultiCheckoutRequestItem(it.index, it.productID.toIntOrZero(), it.checkoutFields, "")
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

    fun createRefreshActionParams(uuids: List<String>, month: Int, year: Int, source: Int? = null): Map<String, Any> {
        val map = mutableMapOf(PARAM_UUIDS to uuids, PARAM_MONTH to month, PARAM_YEAR to year)
        source?.run { map[PARAM_SOURCE] = source }
        return map
    }

    fun createParamDeleteSBM(request : RechargeSBMDeleteBillRequest): Map<String, Any> {
        val map = mutableMapOf(PARAM_DELETE_SBM to request)
        return map
    }

    fun createParamHighlightCategory(): Map<String, Any> {
        val map = mutableMapOf(PARAM_RECHARGE_RECOM to RECHARGE_RECOM_TYPE)
        return map
    }

    fun createParamCloseRecom(uuid: String, contentId: String): Map<String, Any> {
        val request =  RechargeCloseParams(uuid, contentId)
        val map = mutableMapOf(PARAM_CLOSE_RECOM to request)
        return map
    }

    fun convertSBMMultiResponse(typeRestResponseMap: Map<Type, RestResponse?>): DataRechargeMultiCheckoutResponse {
        return typeRestResponseMap[DataRechargeMultiCheckoutResponse::class.java]?.getData() as DataRechargeMultiCheckoutResponse
    }

    fun isHighlightNotEmpty(highlight: HighlightCategoryUiModel): Boolean {
        return highlight.contentId.isNotEmpty()
                && highlight.uuId.isNotEmpty()
                && highlight.applink.isNotEmpty()
    }

    private fun mapperHighlightUiModel(uuid: String, rechargeRecommendations: List<RechargeRecommendationData>): HighlightCategoryUiModel {
        if (rechargeRecommendations.size.isMoreThanZero()){
            val recommendation = rechargeRecommendations.first()
            return HighlightCategoryUiModel(
                recommendation.contentID,
                uuid,
                recommendation.iconURL,
                recommendation.mainText,
                recommendation.subText,
                recommendation.buttonText,
                recommendation.applink
            )
        } else {
            return HighlightCategoryUiModel()
        }
    }

    companion object {
        const val PARAM_LIMIT = "limit"
        const val PARAM_MONTH = "month"
        const val PARAM_YEAR = "year"
        const val PARAM_SOURCE = "source"
        const val PARAM_UUIDS = "uuids"
        const val PARAM_PLATFORM_ID = "platformID"
        const val PARAM_DELETE_SBM = "req"
        const val PARAM_RECHARGE_RECOM = "type"
        const val PARAM_CLOSE_RECOM = "request"

        const val STATEMENT_MONTHS_ERROR = "STATEMENT_MONTHS_ERROR"
        const val STATEMENT_BILLS_ERROR = "STATEMENT_BILLS_ERROR"
        const val MULTI_CHECKOUT_EMPTY_REQUEST = "MULTI_CHECKOUT_EMPTY_REQUEST"

        const val DEFAULT_OS_TYPE = "1"
        const val IDEMPOTENCY_KEY = "Idempotency-Key"
        const val CONTENT_TYPE = "Content-Type"

        const val CACHE_DURATION_MINUTES = 5
        const val RECHARGE_RECOM_TYPE = 3

        const val HARD_CODE_EMPTY_RESPONSE = "error get base data: [favorite] empty favorite data"
    }
}