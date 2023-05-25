package com.tokopedia.topads.dashboard.recommendation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.mapper.GroupDetailMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.AdGroupUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupDetailDataModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.data.model.local.insighttypechips.InsightTypeChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsGroupDetailUseCase
import com.tokopedia.topads.dashboard.recommendation.usecase.TopAdsListAllInsightCountsUseCase
import com.tokopedia.topads.dashboard.recommendation.views.fragments.RecommendationFragment.Companion.TYPE_PRODUCT
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GroupDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsGroupDetailUseCase: TopAdsGroupDetailUseCase,
    private val topAdsListAllInsightCountsUseCase: TopAdsListAllInsightCountsUseCase,
    private val groupDetailMapper: GroupDetailMapper
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

    fun reOrganiseData() {
        _detailPageLiveData.value =
            TopAdsListAllInsightState.Success(groupDetailMapper.reArrangedDataMap())
    }

    fun loadInsightTypeChips(
        adType: String?,
        insightList: ArrayList<AdGroupUiModel>,
        adGroupName: String?,
    ) {
        val list =
            mutableListOf(
                if ("product" == adType) "Iklan Produk" else "Iklan Toko",
                adGroupName ?: ""
            )
        groupDetailMapper.detailPageDataMap[RecommendationConstants.TYPE_INSIGHT] =
            InsightTypeChipsUiModel(list, insightList.toMutableList())
    }

    fun loadDetailPageOnAction(adType: Int, adgroupID: String, isSwitchAdType: Boolean = false) {
        launchCatchError(dispatcher.main, block = {
            if (isSwitchAdType) {
                val data = topAdsListAllInsightCountsUseCase(
                    source = "gql.list_all_insight_counts.test",
                    adGroupType = if (adType == TYPE_PRODUCT) "product" else "headline",
                    insightType = 0
                )
                loadDetailPage(
                    adType,
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupID ?: ""
                )
                val list = mutableListOf(
                    if (adType == TYPE_PRODUCT) "Iklan Produk" else "Iklan Toko",
                    data.topAdsListAllInsightCounts.adGroups.firstOrNull()?.adGroupName ?: ""
                )
                TopAdsListAllInsightState.Success(
                    InsightTypeChipsUiModel(
                        list,
                        groupDetailMapper.toAdGroupUiModelList(data.topAdsListAllInsightCounts.adGroups.toMutableList())
                    )
                )
            } else {
                loadDetailPage(
                    adType,
                    adgroupID
                )
            }
        }, onError = {
                _detailPageLiveData.value = TopAdsListAllInsightState.Fail(it)
            })
    }
}
