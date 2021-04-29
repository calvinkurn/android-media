package com.tokopedia.topads.debit.autotopup.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveSelectionUseCase
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import javax.inject.Named

class TopAdsAutoTopUpViewModel @Inject constructor(
        private val useCase: GraphqlUseCase<TkpdProducts>,
        private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
        private val saveSelectionUseCase: TopAdsSaveSelectionUseCase,
        val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val statusSaveSelection: MutableLiveData<SavingAutoTopUpState> = MutableLiveData()
    fun getAutoTopUpStatusFull() {
        autoTopUpUSeCase.setParams()
        autoTopUpUSeCase.setQuery()
        autoTopUpUSeCase.execute({ data ->
            when {
                data.response == null -> getAutoTopUpStatus.value = Fail(Exception("Gagal mengambil status"))
                data.response.errors.isEmpty() -> getAutoTopUpStatus.value = Success(data.response.data)
                else -> getAutoTopUpStatus.value = Fail(ResponseErrorException(data.response.errors))
            }
        }, {
            getAutoTopUpStatus.value = Fail(it)
        })
    }

    fun saveSelection(isActive: Boolean, selectedItem: AutoTopUpItem) {
        statusSaveSelection.value = Loading
        saveSelectionUseCase.setParam(isActive, selectedItem)
        saveSelectionUseCase.execute({ data ->

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

        }, {
            statusSaveSelection.value = ResponseSaving(false, it)
        })

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