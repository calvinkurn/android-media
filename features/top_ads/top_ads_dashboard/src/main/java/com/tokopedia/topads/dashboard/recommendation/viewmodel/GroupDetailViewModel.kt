package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.model.InsightTypeApplyInput
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.*
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GroupDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGroupDetailUseCase: TopAdsGroupDetailUseCase,
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val groupDetailMapper: GroupDetailMapper,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _detailPageLiveData =
        MutableLiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>()
    val detailPageLiveData: LiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>
        get() = _detailPageLiveData

    private val _editInsightLiveData = MutableLiveData<com.tokopedia.usecase.coroutines.Result<FinalAdResponse>>()
    val editInsightLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<FinalAdResponse>>
        get() = _editInsightLiveData

    fun loadDetailPage(
        adGroupType: Int,
        groupId: String
    ) {
        _detailPageLiveData.value = TopAdsListAllInsightState.Loading(0)
        launchCatchError(dispatcher.main, {
            val data = topAdsGroupDetailUseCase.executeOnBackground(
                groupDetailMapper,
                adGroupType,
                groupId
            )
            _detailPageLiveData.value = TopAdsListAllInsightState.Success(data)
        }, {
            _detailPageLiveData.value = TopAdsListAllInsightState.Fail(it)
        })
    }

    fun reSyncDetailPageData(adGroupType: Int, clickedItem: Int = INVALID_INSIGHT_TYPE) {
        _detailPageLiveData.value =
            TopAdsListAllInsightState.Success(groupDetailMapper.reSyncDetailPageData(adGroupType, clickedItem))
    }

    fun loadInsightTypeChips(
        adType: String?,
        insightList: ArrayList<AdGroupUiModel>,
        adGroupName: String?
    ) {
        val list =
            mutableListOf(
                if (PRODUCT_KEY == adType) "Iklan Produk" else "Iklan Toko",
                adGroupName ?: ""
            )
        groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_INSIGHT] =
            InsightTypeChipsUiModel(list, insightList.toMutableList())
    }

    fun loadDetailPageOnAction(adType: Int, adgroupID: String, insightType: Int, isSwitchAdType: Boolean = false, groupName: String = "") {
        launchCatchError(dispatcher.main, block = {
            if (isSwitchAdType) {
                val data = topAdsListAllInsightCountsUseCase(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = if (adType == TYPE_PRODUCT_VALUE) PRODUCT_KEY else HEADLINE_KEY,
                    insightType = 0
                )
                loadDetailPage(
                    adType,
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupID ?: ""
                )
                val list = mutableListOf(
                    if (adType == TYPE_PRODUCT_VALUE) "Iklan Produk" else "Iklan Toko",
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupName ?: ""
                )
                groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_INSIGHT] =
                    InsightTypeChipsUiModel(
                        list,
                        groupDetailMapper.toAdGroupUiModelList(
                            data.topAdsListAllInsightCounts.adGroups.toMutableList(),
                            insightType
                        )
                    )
            } else {
                loadDetailPage(
                    adType,
                    adgroupID
                )

                val list = mutableListOf(
                    if (adType == TYPE_PRODUCT_VALUE) "Iklan Produk" else "Iklan Toko",
                    groupName
                )
                groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_INSIGHT] =
                    InsightTypeChipsUiModel(
                        list,
                        (groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_INSIGHT] as InsightTypeChipsUiModel).adGroupList
                    )
            }
        }, onError = {
                _detailPageLiveData.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    fun checkIfGroupChipsAvailable(): Boolean {
        return groupDetailMapper.detailPageDataMap[TYPE_CHIPS] != null
    }

    fun selectDefaultChips(insightType: Int) {
        chipsList.forEachIndexed { index, groupDetailChipsItemUiModel ->
            groupDetailChipsItemUiModel.isSelected = (index == insightType)
        }
    }

    fun applyInsight(insightTypeApplyInput: InsightTypeApplyInput) {
        val requestParams = topAdsCreateUseCase.createRequestParamForInsight(insightTypeApplyInput)
        launchCatchError(dispatcher.main, block = {
            topAdsCreateUseCase.execute(requestParams)
        }, onError = {})
    }

    fun applyInsight2(topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput?, groupId: String?) {
        topAdsManagePromoGroupProductInput?.let {
            it.groupID = groupId ?: ""
            it.shopID = userSession.shopId
            it.source = "test-gql"
            val requestParams =
                topAdsCreateUseCase.createRequestParamForInsight2(it)
            launchCatchError(dispatcher.main, block = {
                val data = topAdsCreateUseCase.execute(requestParams)
                _editInsightLiveData.value = Success(data)
            }, onError = {})
        }
    }

    fun getInputDataFromMapper(insightType: Int?): TopadsManagePromoGroupProductInput?{
        return when(insightType){
            TYPE_POSITIVE_KEYWORD -> ((groupDetailMapper.detailPageDataMap.getOrDefault(insightType, null) as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKataKunciUiModel)?.input
            TYPE_KEYWORD_BID -> ((groupDetailMapper.detailPageDataMap.getOrDefault(insightType, null) as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKeywordBidUiModel)?.input
            TYPE_GROUP_BID -> ((groupDetailMapper.detailPageDataMap.getOrDefault(insightType, null) as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianGroupBidUiModel)?.input
            TYPE_DAILY_BUDGET -> ((groupDetailMapper.detailPageDataMap.getOrDefault(insightType, null) as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianDailyBudgetUiModel)?.input
            TYPE_NEGATIVE_KEYWORD_BID -> ((groupDetailMapper.detailPageDataMap.getOrDefault(insightType, null) as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianNegativeKeywordUiModel)?.input
            else -> null
        }
    }

}
