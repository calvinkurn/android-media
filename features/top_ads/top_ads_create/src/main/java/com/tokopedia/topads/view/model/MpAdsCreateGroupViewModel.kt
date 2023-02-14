package com.tokopedia.topads.view.model

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.constants.MpTopadsConst.CREATE_GROUP_PAGE
import javax.inject.Inject

class MpAdsCreateGroupViewModel @Inject constructor(
    private val bidInfoUseCase: BidInfoUseCase,
    private val topAdsGetProductUseCase: TopAdsGetProductUseCase,
    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    fun getBidInfo(
        suggestions: List<DataSuggestions>,
        sourceValue: String,
        onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit
    ) {
        bidInfoUseCase.setParams(suggestions, ParamObject.AUTO_BID_STATE, sourceValue)
        bidInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topadsBidInfo.data)
            },
            { throwable ->
                throwable.printStackTrace()
            }
        )
    }

    fun getProduct(productid: String?, onSuccess: (TopAdsProductResponse) -> Unit) {
        topAdsGetProductUseCase.getProduct(productid) {
            onSuccess(it)
        }
    }

    fun validateGroup(
        groupName: String,
        onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateNameV2) -> Unit)
    ) {
        topAdsGroupValidateNameUseCase.setParams(groupName, CREATE_GROUP_PAGE)
        topAdsGroupValidateNameUseCase.execute({
            onSuccess(it.topAdsGroupValidateName)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun topAdsCreate(
        productIds: List<String>,
        currentGroupName: String,
        dailyBudget: Double,
        onSuccess: ((String) -> Unit),
        onError: ((error: String?) -> Unit)
    ) {
        val param =
            topAdsCreateUseCase.createRequestParamActionCreate(
                productIds,
                currentGroupName,
                0.0,
                0.0,
                dailyBudget,
                CREATE_GROUP_PAGE
            )
        launchCatchError(block = {
            val response = topAdsCreateUseCase.execute(param)

            val dataGroup = response.topadsManageGroupAds.groupResponse
            val dataKeyword = response.topadsManageGroupAds.keywordResponse
            if (dataGroup.errors.isNullOrEmpty() && dataKeyword.errors.isNullOrEmpty()) {
                onSuccess(dataGroup.data.id)
            } else {
                val error =
                    dataGroup.errors?.firstOrNull()?.detail + dataKeyword.errors?.firstOrNull()?.detail
                onError(error)
            }
        }, onError = {
            onError(it.message)
            it.printStackTrace()
        })
    }

    fun getTopAdsDeposit(
        onSuccessGetDeposit: ((DepositAmount) -> Unit)
    ) {
        topAdsGetDepositUseCase.execute({
            onSuccessGetDeposit(it.topadsDashboardDeposits.data)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    override fun onCleared() {
        super.onCleared()
        bidInfoUseCase.cancelJobs()
    }
}
