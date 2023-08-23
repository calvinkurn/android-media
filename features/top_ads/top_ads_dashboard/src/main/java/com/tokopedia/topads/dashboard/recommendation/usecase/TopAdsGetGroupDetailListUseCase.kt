package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.dashboard.data.model.TotalProductKeyResponse
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class TopAdsGetGroupDetailListUseCase @Inject constructor(
    private val topAdsGetDashboardGroupsV3UseCase: TopAdsGetDashboardGroupsV3UseCase,
    private val topAdsGetTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase,
    private val mapper: ProductRecommendationMapper,
) {

    suspend fun executeOnBackground(search: String): List<GroupItemUiModel> {
        return coroutineScope {
            val groupList = getTopadsGroups(search)
            var groupIds = listOf<String>()
            when (val data = groupList) {
                is TopadsProductListState.Success -> {
                    mapper.groupList = data.data.getTopadsDashboardGroups.data
                    groupIds = mapper.getListOfGroupIds(data.data)
                }
                else -> {}
            }
            val groupAdsKeyword = getTopadsTotalAdsAndKeywords(groupIds)

            when(val data = groupAdsKeyword){
                is TopadsProductListState.Success -> {
                    mapper.totalAdsKeywords = data.data.topAdsGetTotalAdsAndKeywords.data
                } else -> {}
            }

            return@coroutineScope mapper.convertToGroupItemUiModel(mapper.groupList, mapper.totalAdsKeywords)
        }
    }

    private suspend fun getTopadsGroups(
        search: String,
    ): TopadsProductListState<DashGroupListResponse> {
        return try {
            topAdsGetDashboardGroupsV3UseCase(search)
        } catch (e: Exception) {
            TopadsProductListState.Fail(e)
        }
    }

    private suspend fun getTopadsTotalAdsAndKeywords(
        groupIds: List<String>
    ): TopadsProductListState<TotalProductKeyResponse> {
        return try {
            topAdsGetTotalAdsAndKeywordsUseCase(groupIds)
        } catch (e: Exception) {
            TopadsProductListState.Fail(e)
        }
    }
}
