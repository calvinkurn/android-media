package com.tokopedia.digital.home.old.domain

import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageSectionMapper.mapItemsToSearchCategoryModels
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageQueries
import com.tokopedia.digital.home.presentation.viewmodel.RechargeHomepageViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException

class DigitalHomepageSearchByDynamicIconUseCase(graphqlRepository: GraphqlRepository) : GraphqlUseCase<RechargeHomepageSections.Response>(graphqlRepository) {
    suspend fun searchCategoryList(mapParams: Map<String, Any>, searchQuery: String): List<DigitalHomePageSearchCategoryModel> {
        setGraphqlQuery(RechargeHomepageQueries.SECTION_QUERY)
        setTypeClass(RechargeHomepageSections.Response::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build())
        setRequestParams(mapParams)

        val rawCategoryData = mapItemsToSearchCategoryModels(executeOnBackground().response)
        var filteredData = rawCategoryData.filter { it.label.contains(searchQuery, true) }
        // Add search query to model for query highlight
        filteredData = filteredData.map { item ->
            item.searchQuery = searchQuery
            return@map item
        }
        return filteredData
    }

    companion object {
        fun createRechargeHomepageSectionsParams(platformId: Int, sectionIDs: List<Int>, enablePersonalize: Boolean = false): Map<String, Any> {
            return mapOf(
                    RechargeHomepageViewModel.PARAM_RECHARGE_HOMEPAGE_SECTIONS_PLATFORM_ID to platformId,
                    RechargeHomepageViewModel.PARAM_RECHARGE_HOMEPAGE_SECTIONS_SECTION_IDS to sectionIDs,
                    RechargeHomepageViewModel.PARAM_RECHARGE_HOMEPAGE_SECTIONS_PERSONALIZE to enablePersonalize
            )
        }
    }
}