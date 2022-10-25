package com.tokopedia.topads.edit.view.model

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.EDIT_PAGE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.edit.usecase.EditSingleAdUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 6/4/20.
 */

private const val KEYWORD_STATUS_KEY1 = 1
private const val KEYWORD_STATUS_KEY2 = 3

class EditFormDefaultViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val bidInfoDefaultUseCase: BidInfoUseCase,
    private val getAdsUseCase: GetAdsUseCase,
    private val getAdKeywordUseCase: GetAdKeywordUseCase,
    private val groupInfoUseCase: GroupInfoUseCase,
    private val editSingleAdUseCase: EditSingleAdUseCase,
    private val getAdInfoUseCase: TopAdsGetPromoUseCase,
    private val userSession: UserSessionInterface,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
) : BaseViewModel(dispatcher) {

    fun validateGroup(
        groupName: String,
        onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateNameV2) -> Unit),
    ) {
        validGroupUseCase.setParams(groupName, Constants.SOURCE_ANDROID_EDIT_GROUP)
        validGroupUseCase.execute(
            {
                onSuccess(it.topAdsGroupValidateName)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getBidInfoDefault(
        suggestions: List<DataSuggestions>,
        onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit,
    ) {
        bidInfoDefaultUseCase.setParams(suggestions, ParamObject.GROUP)
        bidInfoDefaultUseCase.executeQuerySafeMode(
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

    fun getAds(
        page: Int,
        groupId: String?,
        source: String,
        onSuccess: (List<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem>, total: Int, perPage: Int) -> Unit,
    ) {

        getAdsUseCase.setParams(page, groupId, source)
        getAdsUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topadsGetListProductsOfGroup.data,
                    it.topadsGetListProductsOfGroup.page.total,
                    it.topadsGetListProductsOfGroup.page.perPage)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getGroupInfo(
        groupId: String,
        onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit,
    ) {

        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topAdsGetPromoGroup?.data!!)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getAdKeyword(
        groupId: Int,
        cursor: String,
        onSuccess: (List<GetKeywordResponse.KeywordsItem>, cursor: String) -> Unit,
    ) {
        getAdKeywordUseCase.setParams(groupId,
            cursor,
            userSession.shopId,
            source = ParamObject.KEYWORD_SOURCE,
            keywordStatus = listOf(
                KEYWORD_STATUS_KEY1, KEYWORD_STATUS_KEY2))
        getAdKeywordUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topAdsListKeyword.data.keywords,
                    it.topAdsListKeyword.data.pagination.cursor)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getBidInfo(
        suggestions: List<DataSuggestions>,
        onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit,
    ) {
        bidInfoUseCase.setParams(suggestions, KEYWORD)
        bidInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topadsBidInfo.data)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun topAdsCreated(
        dataPro: Bundle,
        dataKey: HashMap<String, Any?>,
        dataGrp: HashMap<String, Any?>,
        onSuccess: (() -> Unit),
        onError: ((error: String?) -> Unit),
    ) {
        launchCatchError(block = {
            val param = topAdsCreateUseCase.setParam(EDIT_PAGE, dataPro, dataKey, dataGrp)

            val response = topAdsCreateUseCase.execute(param)

            val dataGroup = response.topadsManageGroupAds.groupResponse
            val dataKeyword = response.topadsManageGroupAds.keywordResponse
            if (dataGroup.errors.isNullOrEmpty() && dataKeyword.errors.isNullOrEmpty())
                onSuccess()
            else {
                val error =
                    dataGroup.errors?.firstOrNull()?.detail + dataKeyword.errors?.firstOrNull()?.detail
                onError(error)
            }
        }, onError = {
            onError(it.message)
            it.printStackTrace()
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
        bidInfoDefaultUseCase.cancelJobs()
        getAdKeywordUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
        editSingleAdUseCase.cancelJobs()
        getAdInfoUseCase.cancelJobs()
    }
}

