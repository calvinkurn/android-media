package com.tokopedia.topads.debit.autotopup.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class TopAdsAutoTopUpViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   private val userSessionInterface: UserSessionInterface,
                                                   private val useCase: GraphqlUseCase<TkpdProducts>,
                                                   @Named("Main")
                                                   val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val statusSaveSelection = MutableLiveData<SavingAutoTopUpState>()

    @GqlQuery("TopUpStatus", QUERY)
    fun getAutoTopUpStatusFull() {
        val params = mapOf(PARAM_SHOP_ID to userSessionInterface.shopId)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(TopUpStatus.GQL_QUERY, AutoTopUpData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<AutoTopUpData.Response>()

            when {
                data.response == null -> {
                    getAutoTopUpStatus.value = Fail(Exception("Tidak ada data"))
                }
                data.response.errors.isEmpty() -> {
                    getAutoTopUpStatus.value = Success(data.response.data)
                }
                else -> {
                    getAutoTopUpStatus.value = Fail(ResponseErrorException(data.response.errors))
                }
            }
        }) {
            getAutoTopUpStatus.value = Fail(it)
        }
    }

    fun saveSelection(rawQuery: String, isActive: Boolean, selectedItem: AutoTopUpItem) {
        statusSaveSelection.value = Loading
        val params = mapOf(PARAM_SHOP_ID to userSessionInterface.shopId.toIntOrZero(),
                PARAM_ACTION to (if (isActive) TOGGLE_ON else TOGGLE_OFF),
                PARAM_SELECTION_ID to selectedItem.id)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, AutoTopUpData.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<AutoTopUpData.Response>()

            when {
                data.response == null -> {
                    throw Exception("Tidak ada data")
                }
                data.response.errors.isEmpty() -> {
                    statusSaveSelection.value = ResponseSaving(true, null)
                }
                else -> {
                    throw ResponseErrorException(data.response.errors)
                }
            }
        }) {
            statusSaveSelection.value = ResponseSaving(false, it)
        }
    }

    @GqlQuery("CategoryList", TKPD_PRODUCT)
    fun populateCreditList(shopId: String, onSuccess: ((CreditResponse) -> Unit)) {
        val params = mapOf(ParamObject.SHOP_id to shopId,
                ParamObject.SOURCE to TopAdsDashboardConstant.SOURCE_DASH)
        useCase.setTypeClass(TkpdProducts::class.java)
        useCase.setRequestParams(params)
        useCase.setGraphqlQuery(CategoryList.GQL_QUERY)
        useCase.execute({
            onSuccess(it.tkpdProduct.creditResponse)
        }
                , {
            it.printStackTrace()
        })
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_ACTION = "action"
        private const val PARAM_SELECTION_ID = "selectionId"
        private const val TOGGLE_ON = "toggle_on"
        private const val TOGGLE_OFF = "toggle_off"
        const val QUERY = """query topAdsAutoTopup(${'$'}shopId: String!) {
    topAdsAutoTopup(shop_id: ${'$'}shopId){
        data {
            status
            status_desc
            tkpd_product_id
            extra_credit_percent
            available_nominal {
                min_credit_fmt
                price_fmt
                tkpd_product_id
            }
        }
        errors {
            Code
            Detail
            Title
        }
    }
}"""
        const val TKPD_PRODUCT = """query topadsGetTkpdProduct(${'$'}shop_id: String!, ${'$'}source: String!) {
  topadsGetTkpdProduct(shop_id: ${'$'}shop_id, source: ${'$'}source) {
    data {
      credit {
        product_id
        product_type
        product_price
        product_url
        default
        product_name
        min_credit
      }
      extra_credit_percent
    }
  }
}
"""
    }
}