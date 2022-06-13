package com.tokopedia.topads.view.model

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase
) : BaseViewModel(dispatcher.main) {

    fun getTopAdsDeposit(
        onSuccessGetDeposit: ((DepositAmount) -> Unit),
        onErrorGetAds: ((Throwable) -> Unit)
    ) {
        topAdsGetShopDepositUseCase.execute({
            onSuccessGetDeposit(it.topadsDashboardDeposits.data)
        }, {
            onErrorGetAds(it)
        })
    }

    fun validateGroup(
        groupName: String,
        onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit)
    ) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.execute({
            onSuccess(it.topAdsGroupValidateName)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }


    fun topAdsCreated(
        dataPro: Bundle, dataKey: HashMap<String, Any?>,
        dataGrp: HashMap<String, Any?>, onSuccess: (() -> Unit), onError: ((error: String?) -> Unit)
    ) {
        launchCatchError(block = {
            val param =
                topAdsCreateUseCase.setParam(ParamObject.CREATE_PAGE, dataPro, dataKey, dataGrp)

            val topadsManageGroupAds = topAdsCreateUseCase.execute(param).topadsManageGroupAds
            val dataGroup = topadsManageGroupAds.groupResponse
            val dataKeyword = topadsManageGroupAds.keywordResponse

            if (dataGroup.errors.isNullOrEmpty() && dataKeyword.errors.isNullOrEmpty())
                onSuccess()
            else {
                val error = dataGroup.errors?.firstOrNull()?.detail + dataKeyword.errors?.firstOrNull()?.detail
                onError(error)
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    public override fun onCleared() {
        super.onCleared()
        validGroupUseCase.cancelJobs()
        topAdsGetShopDepositUseCase.cancelJobs()
    }
}