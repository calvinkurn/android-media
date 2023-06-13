package com.tokopedia.topads.dashboard.recommendation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.CONST_1
import com.tokopedia.topads.common.data.model.InsightTypeApplyInput
import com.tokopedia.topads.common.data.response.TopadsManagePromoGroupProductInput
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.HEADLINE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INVALID_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PRODUCT_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_CHIPS
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.TYPE_PRODUCT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.Utils
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.data.ChipsData.chipsList
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGetTotalAdGroupsWithInsightUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GroupDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGroupDetailUseCase: TopAdsGroupDetailUseCase,
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase,
    private val topAdsGetTotalAdGroupsWithInsightUseCase: TopAdsGetTotalAdGroupsWithInsightUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val groupDetailMapper: GroupDetailMapper,
    private val utils: Utils
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private val _detailPageLiveData =
        MutableLiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>()
    val detailPageLiveData: LiveData<TopAdsListAllInsightState<Map<Int, GroupDetailDataModel>>>
        get() = _detailPageLiveData

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
            Log.d("svfjbv", "getSellerPerformance: $data")
        }, {
            Log.d("svfjbv", "getSellerPerformance: ${it.message}")
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
//                    source = "gql.list_all_insight_counts.test",
                    source = SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE,
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

    fun applyInsight2(topAdsManagePromoGroupProductInput: TopadsManagePromoGroupProductInput) {
        val requestParams =
            topAdsCreateUseCase.createRequestParamForInsight2(topAdsManagePromoGroupProductInput)
        launchCatchError(dispatcher.main, block = {
            topAdsCreateUseCase.execute(requestParams)
        }, onError = {})
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

    fun getItemListUiModel(titleList: List<String>, adGroupType: String?): List<ItemListUiModel> {
        val list = arrayListOf<ItemListUiModel>()
        if (!groupDetailMapper.insightCountMap[TYPE_PRODUCT_VALUE].isZero()) {
            list.add(
                ItemListUiModel(
                    adType = TYPE_PRODUCT_VALUE,
                    title = titleList.firstOrNull() ?: "",
                    isSelected = (PRODUCT_KEY == adGroupType)
                )
            )
        }
        if (!groupDetailMapper.insightCountMap[RecommendationConstants.TYPE_SHOP_VALUE].isZero()) {
            list.add(
                ItemListUiModel(
                    adType = RecommendationConstants.TYPE_SHOP_VALUE,
                    title = titleList.getOrNull(CONST_1) ?: "",
                    isSelected = (adGroupType != PRODUCT_KEY)
                )
            )
        }
        return list
    }
}
