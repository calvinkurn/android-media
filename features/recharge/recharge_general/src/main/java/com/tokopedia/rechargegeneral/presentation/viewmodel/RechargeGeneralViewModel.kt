package com.tokopedia.rechargegeneral.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.RechargeAddBillsProductTrackData
import com.tokopedia.common.topupbills.data.RechargeSBMAddBillRequest
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.rechargegeneral.model.AddSmartBills
import com.tokopedia.rechargegeneral.model.RechargeGeneralDynamicInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralDppoConsentUiModel
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralProductSelectData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeGeneralViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val getDppoConsentUseCase: GetDppoConsentUseCase,
    private val rechargeGeneralMapper: RechargeGeneralMapper,
    private val dispatcher: CoroutineDispatchers
) :
    BaseViewModel(dispatcher.io) {

    private val mutableOperatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val operatorCluster: LiveData<Result<RechargeGeneralOperatorCluster>>
        get() = mutableOperatorCluster

    private val mutableProductList = MutableLiveData<Result<RechargeGeneralDynamicInput>>()
    val productList: LiveData<Result<RechargeGeneralDynamicInput>>
        get() = mutableProductList

    private val mutableAddBills = MutableLiveData<Result<AddSmartBills>>()
    val addBills: LiveData<Result<AddSmartBills>>
        get() = mutableAddBills

    private val mutableDppoConsent = MutableLiveData<Result<RechargeGeneralDppoConsentUiModel>>()
    val dppoConsent: LiveData<Result<RechargeGeneralDppoConsentUiModel>>
        get() = mutableDppoConsent

    var productListJob: Job? = null

    fun getOperatorCluster(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false, nullErrorMessage: String) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralOperatorCluster.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeGeneralOperatorCluster.Response>()

            if (data.response.operatorGroups == null) {
                throw MessageErrorException(nullErrorMessage)
            } else {
                mutableOperatorCluster.postValue(Success(data.response))
            }
        }) {
            mutableOperatorCluster.postValue(Fail(it))
        }
    }

    fun getProductList(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false, nullErrorMessage: String) {
        productListJob?.cancel()
        productListJob = launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralDynamicInput.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher.io) {
                graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<RechargeGeneralDynamicInput.Response>()

            val foundProduct = data.response.enquiryFields.find { it.name == PARAM_PRODUCT }
            if (foundProduct == null) {
                throw MessageErrorException(nullErrorMessage)
            } else {
                mutableProductList.postValue(Success(data.response))
            }
        }) {
            mutableProductList.postValue(Fail(it))
        }
    }

    fun addBillRecharge(mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(
                    CommonTopupBillsGqlQuery.ADD_BILL_QUERY,
                    AddSmartBills::class.java,
                    mapParam
                )
                graphqlRepository.response(
                    listOf(graphqlRequest),
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                )
            }.getSuccessData<AddSmartBills>()

            mutableAddBills.postValue(Success(data))
        }) {
            mutableAddBills.postValue(Fail(it))
        }
    }

    fun getDppoConsent(categoryId: Int) {
        launchCatchError(block = {
            val data = getDppoConsentUseCase.execute(categoryId)
            val uiData = rechargeGeneralMapper.mapDppoConsentToUiModel(data)
            mutableDppoConsent.postValue(Success(uiData))
        }) {
            mutableDppoConsent.postValue(Fail(it))
        }
    }

    fun createOperatorClusterParams(menuID: Int): Map<String, Int> {
        return mapOf(PARAM_MENU_ID to menuID)
    }

    fun createProductListParams(menuID: Int, operator: String): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuID, PARAM_OPERATOR to operator)
    }

    fun createAddBillsParam(addBillRequest: RechargeSBMAddBillRequest): Map<String, Any> {
        return mapOf(PARAM_ADD_REQUEST to addBillRequest)
    }

    fun createProductAddBills(
        products: List<RechargeGeneralProductSelectData>,
        categoryName: String,
        operatorName: String
    ): List<RechargeAddBillsProductTrackData> {
        return products.mapIndexed { index, product ->
            RechargeAddBillsProductTrackData(
                index,
                operatorName,
                categoryName,
                product.id,
                product.title,
                "",
                product.price
            )
        }
    }

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
        const val NULL_PRODUCT_ERROR = "null product"
        const val PARAM_PRODUCT = "product_id"
        const val PARAM_ADD_REQUEST = "addRequest"
    }
}
