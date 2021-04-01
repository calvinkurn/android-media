package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.edit.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.usecase.EditSingleAdUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.edit.usecase.TopAdsCreateUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 6/4/20.
 */

class EditFormDefaultViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
        private val bidInfoUseCase: BidInfoUseCase,
        private val getAdsUseCase: GetAdsUseCase,
        private val getAdKeywordUseCase: GetAdKeywordUseCase,
        private val groupInfoUseCase: GroupInfoUseCase,
        private val editSingleAdUseCase: EditSingleAdUseCase,
        private val getAdInfoUseCase: TopAdsGetPromoUseCase,
        private val userSession: UserSessionInterface,
        private val topAdsCreateUseCase: TopAdsCreateUseCase) : BaseViewModel(dispatcher) {

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

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, ParamObject.PRODUCT)
        bidInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsBidInfo.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun editSingleAd(adId: String, priceBid: Float, priceDaily: Float) {
        editSingleAdUseCase.setParams(adId, priceBid, priceDaily)
        editSingleAdUseCase.executeQuerySafeMode(
                {
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getAds(page: Int, groupId: Int?, onSuccess: (List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>, total: Int, perPage: Int) -> Unit) {

        getAdsUseCase.setParams(page, groupId)
        getAdsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsGetListProductsOfGroup.data, it.topadsGetListProductsOfGroup.page.total, it.topadsGetListProductsOfGroup.page.perPage)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getGroupInfo(groupId: String, onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit) {

        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetPromoGroup?.data!!)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getAdKeyword(groupId: Int, cursor: String, onSuccess: (List<GetKeywordResponse.KeywordsItem>, cursor: String) -> Unit) {
        getAdKeywordUseCase.setParams(groupId, cursor, userSession.shopId, source = ParamObject.KEYWORD_SOURCE, keywordStatus = listOf(1, 3))
        getAdKeywordUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsListKeyword.data.keywords, it.topAdsListKeyword.data.pagination.cursor)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, KEYWORD)
        bidInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsBidInfo.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun topAdsCreated(dataPro: Bundle, dataKey: HashMap<String, Any?>,
                      dataGrp: HashMap<String, Any?>, onSuccess: (() -> Unit), onError: ((error: String?) -> Unit)) {
        val param = topAdsCreateUseCase.setParam(dataPro, dataKey, dataGrp)
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
    }


    fun getSingleAdInfo(adId: String, onSuccess: ((List<SingleAd>) -> Unit)) {
        getAdInfoUseCase.setParams(adId, userSession.shopId)
        getAdInfoUseCase.execute(
                {
                    onSuccess(it.topAdsGetPromo.data)
                },
                {
                    it.printStackTrace()
                }
        )
    }

    public override fun onCleared() {
        super.onCleared()
        validGroupUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        getAdsUseCase.cancelJobs()
        getAdKeywordUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
        topAdsCreateUseCase.unsubscribe()
        editSingleAdUseCase.cancelJobs()
        getAdInfoUseCase.cancelJobs()
    }
}

