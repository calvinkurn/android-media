package com.tokopedia.topads.dashboard.recommendation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.top_ads_headline_usecase.CreateHeadlineAdsUseCase
import com.tokopedia.top_ads_headline_usecase.model.TopadsManageHeadlineAdResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.DEFAULT_LOADING
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_INSIGHT_MUTATION_SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_INSIGHT_MUTATION_SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_PRODUCT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TAB_NAME_SHOP
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_INSIGHT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_NEGATIVE_KEYWORD_BID
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_SHOP_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianDailyBudgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianGroupBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKataKunciUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianKeywordBidUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AccordianNegativeKeywordUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupInsightsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ListBottomSheetItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.groupdetailchips.GroupDetailChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
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
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val groupDetailMapper: GroupDetailMapper,
    private val createHeadlineAdsUseCase: CreateHeadlineAdsUseCase,
    private val utils: Utils,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _detailPageLiveData =
        MutableLiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>()
    val detailPageLiveData: LiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>
        get() = _detailPageLiveData

    private val _editInsightLiveData = MutableLiveData<com.tokopedia.usecase.coroutines.Result<FinalAdResponse>>()
    val editInsightLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<FinalAdResponse>>
        get() = _editInsightLiveData

    private val _editHeadlineInsightLiveData = MutableLiveData<com.tokopedia.usecase.coroutines.Result<TopadsManageHeadlineAdResponse.Data>>()
    val editHeadlineInsightLiveData: LiveData<com.tokopedia.usecase.coroutines.Result<TopadsManageHeadlineAdResponse.Data>>
        get() = _editHeadlineInsightLiveData

    fun loadDetailPage(
        adGroupType: Int,
        groupId: String
    ) {
        _detailPageLiveData.value = TopAdsListAllInsightState.Loading(DEFAULT_LOADING)
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

    fun reSyncDetailPageData(adGroupType: Int, clickedItem: Int = INVALID_INSIGHT_TYPE, clickedChips: Int = INVALID_INSIGHT_TYPE) {
        _detailPageLiveData.value =
            TopAdsListAllInsightState.Success(groupDetailMapper.reSyncDetailPageData(adGroupType, clickedItem, clickedChips))
    }

    fun loadInsightTypeChips(
        adType: String?,
        insightList: ArrayList<AdGroupUiModel>,
        adGroupName: String?
    ) {
        val list =
            mutableListOf(
                if (PRODUCT_KEY == adType) TAB_NAME_PRODUCT else TAB_NAME_SHOP,
                adGroupName ?: ""
            )
        groupDetailMapper.detailPageDataMap[TYPE_INSIGHT] =
            InsightTypeChipsUiModel(list, insightList.toMutableList())
    }

    fun loadDetailPageOnAction(adType: Int, adgroupID: String, insightType: Int, isSwitchAdType: Boolean = false, groupName: String = "") {
        launchCatchError(dispatcher.main, block = {
            if (isSwitchAdType) {
                val data = topAdsListAllInsightCountsUseCase(
                    source = SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE,
                    adGroupType = if (adType == TYPE_PRODUCT_VALUE) PRODUCT_KEY else HEADLINE_KEY,
                    insightType = TYPE_INSIGHT
                )
                loadDetailPage(
                    adType,
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupID ?: ""
                )
                val list = mutableListOf(
                    if (adType == TYPE_PRODUCT_VALUE) TAB_NAME_PRODUCT else TAB_NAME_SHOP,
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupName ?: ""
                )
                groupDetailMapper.detailPageDataMap[TYPE_INSIGHT] =
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
                    if (adType == TYPE_PRODUCT_VALUE) TAB_NAME_PRODUCT else TAB_NAME_SHOP,
                    groupName
                )
                groupDetailMapper.detailPageDataMap[TYPE_INSIGHT] =
                    InsightTypeChipsUiModel(
                        list,
                        (groupDetailMapper.detailPageDataMap[TYPE_INSIGHT] as InsightTypeChipsUiModel).adGroupList
                    )
            }
        }, onError = {
                _detailPageLiveData.value = TopAdsListAllInsightState.Fail(it)
            })
    }

    fun checkIfGroupChipsAvailable(): Boolean {
        return groupDetailMapper.detailPageDataMap[TYPE_CHIPS] != null
    }

    fun selectDefaultChips(insightType: Int, adType: Int = TYPE_PRODUCT_VALUE) {
        if (adType == TYPE_PRODUCT_VALUE) groupDetailMapper.detailPageDataMap[TYPE_CHIPS] = GroupDetailChipsUiModel()
        chipsList.forEachIndexed { index, groupDetailChipsItemUiModel ->
            groupDetailChipsItemUiModel.isSelected = (index == insightType)
        }
    }

    fun submitInsights(
        input: TopadsManagePromoGroupProductInput?,
        groupId: String?,
        adType: String?,
        InsightType: Int?,
        groupName: String?
    ) {
        if (adType == HEADLINE_KEY && InsightType == TYPE_POSITIVE_KEYWORD) {
            submitHeadlineInsights(input, groupId, groupName)
        } else {
            submitProductInsights(input, groupId)
        }
    }

    private fun submitHeadlineInsights(
        input: TopadsManagePromoGroupProductInput?,
        groupId: String?,
        groupName: String?
    ) {
        val data = groupDetailMapper.convertToTopAdsManageHeadlineInput2(
            input,
            userSession.shopId,
            groupId,
            HEADLINE_INSIGHT_MUTATION_SOURCE,
            groupName
        )
        launchCatchError(
            block = {
                createHeadlineAdsUseCase.setParams(data)
                val response: TopadsManageHeadlineAdResponse.Data =
                    createHeadlineAdsUseCase.executeOnBackground()
                if (response.topadsManageHeadlineAd.success.id.isNotEmpty()) {
                    _editHeadlineInsightLiveData.value = Success(response)
                }
                if (response.topadsManageHeadlineAd.errors.isNotEmpty()) {
                    _editHeadlineInsightLiveData.value = Fail(Throwable())
                }
            },
            onError = {
                _editHeadlineInsightLiveData.value = Fail(it)
            }
        )
    }

    private fun submitProductInsights(
        input: TopadsManagePromoGroupProductInput?,
        groupId: String?
    ) {
        input?.let {
            it.groupID = groupId ?: ""
            it.shopID = userSession.shopId
            it.source = PRODUCT_INSIGHT_MUTATION_SOURCE
            val requestParams =
                topAdsCreateUseCase.createRequestParamForInsight2(it)
            launchCatchError(dispatcher.main, block = {
                val data = topAdsCreateUseCase.execute(requestParams)
                _editInsightLiveData.value = Success(data)
            }, onError = {
                    _editInsightLiveData.value = Fail(it)
                })
        }
    }

    fun loadInsightCountForOtherAdType(adGroupType: Int) {
        val otherAdType = if (adGroupType == TYPE_PRODUCT_VALUE) HEADLINE_KEY else PRODUCT_KEY
        launchCatchError(dispatcher.main, block = {
            val data = topAdsGetTotalAdGroupsWithInsightUseCase.invoke(
                listOf(otherAdType),
                SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
            )
            if (data is TopAdsListAllInsightState.Success) {
                groupDetailMapper.putInsightCount(
                    utils.convertAdTypeToInt(otherAdType),
                    data.data.topAdsGetTotalAdGroupsWithInsightByShopID.totalAdGroupsWithInsight.totalAdGroupsWithInsight
                )
            }
        }, onError = {})
    }

    fun getItemListUiModel(titleList: List<String>, adGroupType: String?): List<ListBottomSheetItemUiModel> {
        val list = arrayListOf<ListBottomSheetItemUiModel>()
        if (!groupDetailMapper.insightCountMap[TYPE_PRODUCT_VALUE].isZero()) {
            list.add(
                ListBottomSheetItemUiModel(
                    adType = TYPE_PRODUCT_VALUE,
                    title = titleList.firstOrNull() ?: "",
                    isSelected = (PRODUCT_KEY == adGroupType)
                )
            )
        }
        if (!groupDetailMapper.insightCountMap[TYPE_SHOP_VALUE].isZero()) {
            list.add(
                ListBottomSheetItemUiModel(
                    adType = TYPE_SHOP_VALUE,
                    title = titleList.getOrNull(CONST_1) ?: "",
                    isSelected = (adGroupType != PRODUCT_KEY)
                )
            )
        }
        return list
    }

    fun getInputDataFromMapper(insightType: Int?): TopadsManagePromoGroupProductInput? {
        return when (insightType) {
            TYPE_POSITIVE_KEYWORD -> ((groupDetailMapper.detailPageDataMap[insightType] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKataKunciUiModel)?.input
            TYPE_KEYWORD_BID -> ((groupDetailMapper.detailPageDataMap[insightType] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianKeywordBidUiModel)?.input
            TYPE_GROUP_BID -> ((groupDetailMapper.detailPageDataMap[insightType] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianGroupBidUiModel)?.input
            TYPE_DAILY_BUDGET -> ((groupDetailMapper.detailPageDataMap[insightType] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianDailyBudgetUiModel)?.input
            TYPE_NEGATIVE_KEYWORD_BID -> ((groupDetailMapper.detailPageDataMap[insightType] as? GroupInsightsUiModel)?.expandItemDataModel as? AccordianNegativeKeywordUiModel)?.input
            else -> null
        }
    }
}
