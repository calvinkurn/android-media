package com.tokopedia.topads.edit.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.model.TopAdsGetTotalAdsAndKeywords
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopAdsGetBidSuggestionResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.usecase.GetAdKeywordUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetBidSuggestionByProductIDsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsImpressionPredictionSearchUseCase
import com.tokopedia.topads.edit.usecase.GetAdsUseCase
import com.tokopedia.topads.edit.usecase.GroupInfoUseCase
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.SOURCE_EDIT_GROUP
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

private const val KEYWORD_STATUS_KEY1 = 1
private const val KEYWORD_STATUS_KEY2 = 3

class EditAdGroupViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val groupInfoUseCase: GroupInfoUseCase,
    private val getTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase,
    private val validateNameAdGroupUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsImpressionPredictionSearchUseCase: TopAdsImpressionPredictionSearchUseCase,
    private val topAdsImpressionPredictionBrowseUseCase: TopAdsImpressionPredictionBrowseUseCase,
    private val topAdsGetBidSuggestionByProductIDsUseCase: TopAdsGetBidSuggestionByProductIDsUseCase,
    private val getAdKeywordUseCase: GetAdKeywordUseCase,
    private val getAdsUseCase: GetAdsUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val bidInfoUseCaseDefault: BidInfoUseCase,
    private val userSession: UserSessionInterface
) :
    BaseViewModel(dispatchers.main) {

    private var _adsKeywordCount: MutableLiveData<Result<TopAdsGetTotalAdsAndKeywords>> = MutableLiveData()
    val adsKeywordCount: LiveData<Result<TopAdsGetTotalAdsAndKeywords>> = _adsKeywordCount

    private val _searchPerformanceData = MutableLiveData<Result<ImpressionPredictionResponse>>()
    val searchPerformanceData: LiveData<Result<ImpressionPredictionResponse>>
        get() = _searchPerformanceData

    private val _browsePerformanceData = MutableLiveData<Result<ImpressionPredictionResponse>>()
    val browsePerformanceData: LiveData<Result<ImpressionPredictionResponse>>
        get() = _browsePerformanceData

    private val _performanceData = MutableLiveData<Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>>>()
    val performanceData: LiveData<Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>>>
        get() = _performanceData

    private val _bidProductData = MutableLiveData<Result<TopAdsGetBidSuggestionResponse>>()
    val bidProductData: LiveData<Result<TopAdsGetBidSuggestionResponse>>
        get() = _bidProductData
    private val _adsData = MutableLiveData<Result<GetAdProductResponse>>()
    val adsData: LiveData<Result<GetAdProductResponse>>
        get() = _adsData


    fun getGroupInfo(
        groupId: String,
        onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit
    ) {
        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topAdsGetPromoGroup?.data!!)
            },
            { throwable ->
                throwable.printStackTrace()
            }
        )
    }

    fun getTotalAdsAndKeywordsCount(
        groupId: String
    ) {
        launchCatchError(block = {
            val response = getTotalAdsAndKeywordsUseCase(listOf(groupId))
            if (response.topAdsGetTotalAdsAndKeywords.errors.isEmpty()) {
                _adsKeywordCount.value = Success(response.topAdsGetTotalAdsAndKeywords)
            }
        }) {
            _adsKeywordCount.value = Fail(it)
        }
    }

    fun validateGroup(
        groupName: String,
        onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateNameV2) -> Unit),
        onFailure: ((String?) -> Unit)
    ) {
        validateNameAdGroupUseCase.setParams(groupName, Constants.SOURCE_ANDROID_EDIT_GROUP)
        validateNameAdGroupUseCase.execute(
            {
                if (it.topAdsGroupValidateName.errors.isEmpty()) {
                    onSuccess(it.topAdsGroupValidateName)
                } else {
                    onFailure.invoke(it.topAdsGroupValidateName.errors.firstOrNull()?.detail)
                }
            },
            { throwable ->
                throwable.printStackTrace()
                onFailure.invoke(throwable.message)
            })
    }


    fun getAds(
        page: Int,
        groupId: String?,
        source: String,
    ) {
        launchCatchError(block = {
            getAdsUseCase.setParams(page, groupId, source)
            getAdsUseCase.executeQuerySafeMode(
                {
                    _adsData.value = Success(it)

                },
                { throwable ->
                    _adsData.value = Fail(throwable)
                })
        }) {
            _adsData.value = Fail(it)
        }
    }


    fun getBrowsePerformanceData(productIds: List<String>,
                                 finalBid: Float,
                                 initialBid: Float,
                                 dailyBudget: Float) {
        launchCatchError(block = {
            val data = topAdsImpressionPredictionBrowseUseCase.invoke("test", productIds, finalBid, initialBid, dailyBudget)
            if (data is Success) {
                _browsePerformanceData.value = Success(data.data)
            }

        }) {
            _browsePerformanceData.value = Fail(it)
        }
    }


    fun getPerformanceData(productIds: List<String>,
                           bids: MutableList<Float?>,
                           dailyBudget: Float) {
        val list: MutableList<Pair<Int, Int>> = mutableListOf()
        launchCatchError(block = {
            val searchDef = async {
                bids.firstOrNull()?.let { topAdsImpressionPredictionSearchUseCase.invoke(SOURCE_EDIT_GROUP, productIds, it, it, dailyBudget) }
            }
            val browseDef = async {
                bids.getOrNull(CONST_1)?.let { topAdsImpressionPredictionBrowseUseCase.invoke(SOURCE_EDIT_GROUP, productIds, it, it, dailyBudget) }
            }

            val searchData = searchDef.await()
            val browseData = browseDef.await()
            when (searchData) {
                is Success -> {
                    val data = searchData.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    list.add(Pair(data.finalImpression, data.increment))
                }

                else -> {}
            }
            when (browseData) {
                is Success -> {
                    val data = browseData.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    list.add(Pair(data.finalImpression, data.increment))
                }

                else -> {}
            }
            _performanceData.value = getPerformanceDataModel(list, getTotalIncrementPercentage(list))
        }) {}
    }

    private fun getPerformanceDataModel(list: MutableList<Pair<Int, Int>>, totalIncrementPercentage: Int): Result<MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>> {
        val modelList = mutableListOf(
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Di Pencarian", list.first().first.toString(), list.first().second.toString()),
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Di Rekomendasi", list[CONST_1].first.toString(), list[CONST_1].second.toString()),
            CreateEditAdGroupItemAdsPotentialWidgetUiModel("Total Tampil ", (list.first().first + list[CONST_1].first).toString(), totalIncrementPercentage.toString())
        )
        return Success(modelList)

    }

    private fun getTotalIncrementPercentage(list: MutableList<Pair<Int, Int>>): Int {
        val increment = Int.ZERO
        list.forEach {
            if (it.second > Int.ZERO) increment + it.second
        }
        return increment
    }

    fun topAdsCreated(
        dataPro: Bundle,
        dataKey: HashMap<String, Any?>,
        dataGrp: HashMap<String, Any?>,
        onSuccess: (() -> Unit),
        onError: ((error: String?) -> Unit),
    ) {
        launchCatchError(block = {
            val param = topAdsCreateUseCase.setParam(ParamObject.EDIT_PAGE, dataPro, dataKey, dataGrp)

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

    fun getProductBid(productIds: List<String>) {
        launchCatchError(dispatchers.main, block = {
            val data = topAdsGetBidSuggestionByProductIDsUseCase.invoke("adbidinsight.test", productIds)
            _bidProductData.value = data
        }, {
            _bidProductData.value = Fail(it)
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

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        launch(block = {
            bidInfoUseCaseDefault.setParams(suggestions, ParamObject.PRODUCT, ParamObject.SOURCE_VALUE)
            bidInfoUseCaseDefault.executeQuerySafeMode({
                onSuccess(it.topadsBidInfo.data)
            }, {
                it.printStackTrace()
            })
        })
    }
}
