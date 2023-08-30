package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.TotalProductKeyResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.topads.dashboard.recommendation.data.mapper.ProductRecommendationMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupListUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class TopAdsGetGroupDetailListUseCase @Inject constructor(
    private val topAdsGetDashboardGroupsV3UseCase: TopAdsGetDashboardGroupsV3UseCase,
    private val topAdsGetTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase,
) {

    suspend fun executeOnBackground(
        search: String,
        groupType: Int,
        mapper: ProductRecommendationMapper
    ): TopadsProductListState<List<GroupListUiModel>> {
        return coroutineScope {
            val groupList = getTopadsGroups(search, groupType)
            val groupIds: List<String>
            when (val data = groupList) {
                is TopadsProductListState.Success -> {
                    mapper.groupList = data.data.getTopadsDashboardGroups.data
                    groupIds = mapper.getListOfGroupIds(data.data)
                }
                is TopadsProductListState.Empty -> {
                    return@coroutineScope TopadsProductListState.Empty(mapper.getEmptyGroupListDefaultUiModel())
                }
                else -> {
                    return@coroutineScope TopadsProductListState.Fail(Throwable())
                }
            }

            val groupAdsKeyword = getTopadsTotalAdsAndKeywords(groupIds)

            when (val data = groupAdsKeyword) {
                is TopadsProductListState.Success -> {
                    mapper.totalAdsKeywords = data.data.topAdsGetTotalAdsAndKeywords.data
                }
                else -> {
                    return@coroutineScope TopadsProductListState.Fail(Throwable())
                }
            }

            return@coroutineScope TopadsProductListState.Success(
                mapper.convertToGroupItemUiModel(
                    mapper.groupList,
                    mapper.totalAdsKeywords
                )
            )
        }
    }

    private suspend fun getTopadsGroups(
        search: String,
        groupType: Int
    ): TopadsProductListState<DashGroupListResponse> {
        return try {
            val data = topAdsGetDashboardGroupsV3UseCase(search, groupType)
            when {
                data.getTopadsDashboardGroups.errors.isNotEmpty() -> TopadsProductListState.Fail(
                    Exception()
                )
                data.getTopadsDashboardGroups.data.isEmpty() -> TopadsProductListState.Empty(data)
                else -> TopadsProductListState.Success(data)
            }
        } catch (e: Exception) {
            TopadsProductListState.Fail(e)
        }
    }

    private suspend fun getTopadsTotalAdsAndKeywords(
        groupIds: List<String>
    ): TopadsProductListState<TotalProductKeyResponse> {
        return try {
            val data = topAdsGetTotalAdsAndKeywordsUseCase(groupIds)
            when {
                data.topAdsGetTotalAdsAndKeywords.errors.isNotEmpty() -> TopadsProductListState.Fail(
                    Throwable(data.topAdsGetTotalAdsAndKeywords.errors.firstOrNull()?.title)
                )
                data.topAdsGetTotalAdsAndKeywords.data.isEmpty() -> TopadsProductListState.Fail(
                    Exception()
                )
                else -> TopadsProductListState.Success(data)
            }
        } catch (e: Exception) {
            TopadsProductListState.Fail(e)
        }
    }
}
