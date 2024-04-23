package com.tokopedia.rechargegeneral.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_digital.common.di.DigitalCacheEnablerQualifier
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.rechargegeneral.model.RechargeGeneralDynamicInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralOperatorCluster
import com.tokopedia.rechargegeneral.model.mapper.RechargeGeneralMapper
import com.tokopedia.rechargegeneral.presentation.model.RechargeGeneralDppoConsentUiModel
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
    @DigitalCacheEnablerQualifier private val isEnableGqlCache: Boolean,
    private val dispatcher: CoroutineDispatchers
) :
    BaseViewModel(dispatcher.io) {

    private val mutableOperatorCluster = MutableLiveData<Result<RechargeGeneralOperatorCluster>>()
    val operatorCluster: LiveData<Result<RechargeGeneralOperatorCluster>>
        get() = mutableOperatorCluster

    private val mutableProductList = MutableLiveData<Result<RechargeGeneralDynamicInput>>()
    val productList: LiveData<Result<RechargeGeneralDynamicInput>>
        get() = mutableProductList

    private val mutableDppoConsent = MutableLiveData<Result<RechargeGeneralDppoConsentUiModel>>()
    val dppoConsent: LiveData<Result<RechargeGeneralDppoConsentUiModel>>
        get() = mutableDppoConsent

    var productListJob: Job? = null

    fun getOperatorCluster(rawQuery: String, mapParams: Map<String, Any>, isLoadFromCloud: Boolean = false, nullErrorMessage: String) {
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(rawQuery, RechargeGeneralOperatorCluster.Response::class.java, mapParams)
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud || !isEnableGqlCache) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
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
            val graphqlRequest = GraphqlRequest(
                rawQuery,
                RechargeGeneralDynamicInput.Response::class.java,
                mapParams
            )
            val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
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

    companion object {
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_OPERATOR = "operator"
        const val PARAM_PRODUCT = "product_id"
        const val PARAM_ADD_REQUEST = "addRequest"
    }
}
