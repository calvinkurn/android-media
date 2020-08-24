package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.*
import com.tokopedia.topads.edit.usecase.*
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 6/4/20.
 */

class EditFormDefaultViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val validGroupUseCase: ValidGroupUseCase,
        private val bidInfoUseCase: BidInfoUseCase,
        private val getAdsUseCase: GetAdsUseCase,
        private val getAdKeywordUseCase: GetAdKeywordUseCase,
        private val groupInfoUseCase: GroupInfoUseCase,
        private val editSingleAdUseCase: EditSingleAdUseCase,
        private val topAdsCreateUseCase: TopAdsCreateUseCase) : BaseViewModel(dispatcher) {

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit)) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.executeQuerySafeMode(
                {
                    onSuccess(it)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit) {
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
        editSingleAdUseCase.setParams(adId,priceBid,priceDaily)
        editSingleAdUseCase.executeQuerySafeMode(
                {
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getAds(groupId: Int?, onSuccess: (List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>) -> Unit) {

        getAdsUseCase.setParams(groupId)
        getAdsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsGetListProductsOfGroup.data)
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

    fun getAdKeyword(groupId: Int, onSuccess: (List<GetKeywordResponse.KeywordsItem>) -> Unit) {
        getAdKeywordUseCase.setParams(groupId)
        getAdKeywordUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsListKeyword.data.keywords)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, KEYWORD)
        bidInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsBidInfo.data)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun topAdsCreated(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>,
                      dataGroup: HashMap<String, Any?>, onSuccess: (() -> Unit),onError:(()->Unit)) {
        topAdsCreateUseCase.setParam(dataProduct, dataKeyword, dataGroup)
        topAdsCreateUseCase.executeQuerySafeMode(
                { onSuccess() },
                { throwable ->
                    onError()
                    throwable.printStackTrace()
                })
    }

    public override fun onCleared() {
        super.onCleared()
        validGroupUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        getAdsUseCase.cancelJobs()
        getAdKeywordUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
        topAdsCreateUseCase.cancelJobs()
        editSingleAdUseCase.cancelJobs()
    }
}

