package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.RequestHelper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatchers,
        private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
        private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher.main) {

    fun getTopAdsDeposit(onSuccessGetDeposit: ((DepositAmount) -> Unit),
                         onErrorGetAds: ((Throwable) -> Unit)) {

        topAdsGetShopDepositUseCase.execute({
            onSuccessGetDeposit(it.topadsDashboardDeposits.data)
        }
                , {
            onErrorGetAds(it)
        })
    }

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit)) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.execute(
                {
                    onSuccess(it.topAdsGroupValidateName)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }


    fun topAdsCreated(param: HashMap<String, Any>, onSuccessGetDeposit: (() -> Unit),
                      onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_activate_ads),
                                ResponseCreateGroup::class.java, param)
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseCreateGroup>().let {
                        if (it.topadsCreateGroupAds.errors.isNotEmpty())
                            onErrorGetAds(Exception(it.topadsCreateGroupAds.errors.firstOrNull()?.detail))
                        else
                            onSuccessGetDeposit()
                    }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }
}