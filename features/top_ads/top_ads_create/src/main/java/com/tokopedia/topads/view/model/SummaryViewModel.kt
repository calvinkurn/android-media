package com.tokopedia.topads.view.model

import android.content.Context
import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.RequestHelper
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.lang.reflect.Type
import java.util.HashMap
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatchers,
        private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
        private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
        private val topAdsCreateUseCase: TopAdsCreateUseCase,
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


    fun topAdsCreated(dataPro: Bundle, dataKey: HashMap<String, Any?>,
                      dataGrp: HashMap<String, Any?>, onSuccess: (() -> Unit), onError: ((error: String?) -> Unit)) {


        val param = topAdsCreateUseCase.setParam(ParamObject.CREATE_PAGE, dataPro, dataKey, dataGrp)
        topAdsCreateUseCase.execute(param, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<FinalAdResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<FinalAdResponse>
                val dataGroup = response.data?.topadsManageGroupAds?.groupResponse
                val dataKeyword = response.data?.topadsManageGroupAds?.keywordResponse
                if (dataGroup?.errors.isNullOrEmpty() && dataKeyword?.errors.isNullOrEmpty())
                    onSuccess()
                else {
                    var error = ""
                    if (!dataGroup?.errors.isNullOrEmpty())
                        error = dataGroup?.errors?.firstOrNull()?.detail ?: ""
                    else if (!dataKeyword?.errors.isNullOrEmpty())
                        error = dataKeyword?.errors?.firstOrNull()?.detail ?: ""
                    onError(error)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                onError(e?.message)
                e?.printStackTrace()
            }
        })
//        launchCatchError(
//                block = {
//                    val data = withContext(dispatcher.io) {
//                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_activate_ads),
//                                ResponseCreateGroup::class.java, param)
//                        val cacheStrategy = RequestHelper.getCacheStrategy()
//                        repository.getReseponse(listOf(request), cacheStrategy)
//                    }
//                    data.getSuccessData<ResponseCreateGroup>().let {
//                        if (it.topadsCreateGroupAds.errors.isNotEmpty())
//                            onErrorGetAds(Exception(it.topadsCreateGroupAds.errors.firstOrNull()?.detail))
//                        else
//                            onSuccessGetDeposit()
//                    }
//                },
//                onError = {
//                    onErrorGetAds(it)
//                }
//        )
    }
}