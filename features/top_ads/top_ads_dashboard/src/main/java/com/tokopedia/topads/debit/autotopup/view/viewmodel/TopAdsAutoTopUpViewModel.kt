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
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveSelectionUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsTopUpCreditUseCase
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopAdsAutoTopUpViewModel @Inject constructor(
    private val useCase: GraphqlUseCase<TkpdProducts>,
    private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
    private val topAdsTopUpCreditUseCase: TopAdsTopUpCreditUseCase,
    private val saveSelectionUseCase: TopAdsSaveSelectionUseCase,
    val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {

    val getAutoTopUpStatus = MutableLiveData<Result<AutoTopUpStatus>>()
    val statusSaveSelection: MutableLiveData<SavingAutoTopUpState> = MutableLiveData()
    val topAdsTopUpCreditData: MutableLiveData<Result<TopAdsShopTierShopGradeData.ShopInfoByID.Result>> = MutableLiveData()
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
    fun getTopAdsCreditList() {
        topAdsTopUpCreditUseCase.setParams()
        topAdsTopUpCreditUseCase.execute({ data ->
            if (data.shopInfoByID?.result!=null){
                topAdsTopUpCreditData.value = Success(data.shopInfoByID.result.first())
            }
        },
            {
                it.printStackTrace()
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

    fun getCreditItemData2(
        credit: List<DataCredit>?,
        data: TopAdsShopTierShopGradeData.ShopInfoByID.Result
    ): MutableList<TopUpCreditItemData> {
        val nominalList = mutableListOf<TopUpCreditItemData>()

        when (data.goldOS?.shopTier) {
            0 -> {
                nominalList.clear()
                nominalList.addAll(createCreditList(credit, 5.0f))

            }
            1 -> {
                nominalList.clear()
                nominalList.addAll(createCreditList(credit, 5.0f))
            }
            2 -> {
                nominalList.clear()
                nominalList.addAll(createCreditList(credit, 7.0f))
            }
            3 -> {
                when (data.goldOS.shopTier) {
                    24 -> {
                        nominalList.clear()
                        nominalList.addAll(createCreditList(credit, 5.5f))
                    }
                    26 -> {
                        nominalList.clear()
                        nominalList.addAll(createCreditList(credit, 6.0f))
                    }
                    28 -> {
                        nominalList.clear()
                        nominalList.addAll(createCreditList(credit, 6.5f))
                    }
                }

            }
        }
        return nominalList
    }

    private fun createCreditList(credit: List<DataCredit>?, percent: Float): MutableList<TopUpCreditItemData> {
        val nominalList = mutableListOf<TopUpCreditItemData>()
        nominalList.clear()
        credit?.forEach {
            nominalList.add(TopUpCreditItemData(it.productPrice, "Bonus Rp${Utils.convertToCurrencyString((Utils.convertMoneyToValue(it.productPrice)*percent/100).toLong())}"))
        }
        return nominalList
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
