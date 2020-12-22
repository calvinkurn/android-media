package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
        validGroupUseCase.setParams(userSession.shopId.toIntOrZero(), groupName)
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
        getAdKeywordUseCase.setParams(groupId, cursor, userSession.shopId.toIntOrZero())
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

    fun topAdsCreated(dataProduct: Bundle, dataKeyword: HashMap<String, Any?>,
                      dataGroup: HashMap<String, Any?>, onSuccess: (() -> Unit), onError: (() -> Unit)) {
        topAdsCreateUseCase.setParam(dataProduct, dataKeyword, dataGroup)
        topAdsCreateUseCase.executeQuerySafeMode(
                { onSuccess() },
                { throwable ->
                    onError()
                    throwable.printStackTrace()
                })
    }

    fun getSingleAdInfo(adId: String, onSuccess: ((List<SingleAd>) -> Unit)) {
        getAdInfoUseCase.setParams(adId, userSession.shopId)
        getAdInfoUseCase.execute(
                {
                    onSuccessGroup(onSuccess)
                },
                {
                    it.printStackTrace()
                }
        )
    }

    private fun onSuccessGroup(onSuccess: (List<SingleAd>) -> Unit): (SingleAdInFo) -> Unit {
        return {
            onSuccess(it.topAdsGetPromo.data)
        }
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
        getAdInfoUseCase.cancelJobs()
    }
}

