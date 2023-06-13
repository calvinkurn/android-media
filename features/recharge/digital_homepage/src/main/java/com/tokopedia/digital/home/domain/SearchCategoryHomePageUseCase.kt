package com.tokopedia.digital.home.domain

import com.tokopedia.digital.home.model.DigitalHomePageSearchNewModel
import com.tokopedia.digital.home.presentation.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.util.DigitalHomePageCategoryDataMapper
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException

class SearchCategoryHomePageUseCase(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DigitalHomePageCategoryModel>(graphqlRepository) {

    suspend fun searchCategoryList(
        rawQuery: GqlQueryInterface,
        isLoadFromCloud: Boolean,
        searchQuery: String
    ): DigitalHomePageSearchNewModel {
        setGraphqlQuery(rawQuery)
        setTypeClass(DigitalHomePageCategoryModel::class.java)
        setCacheStrategy(
            GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                .build()
        )

        val rawCategoryData =
            DigitalHomePageCategoryDataMapper.mapCategoryData(executeOnBackground())
        rawCategoryData?.run {
            val searchCategoryData = rawCategoryData.map { item ->
                DigitalHomePageSearchCategoryModel(
                    item.id,
                    item.name,
                    item.label,
                    item.applink,
                    item.icon
                )
            }
            var filteredData = searchCategoryData.filter { it.label.contains(searchQuery, true) }
            // Add search query to model for query highlight
            filteredData = filteredData.map { item ->
                item.searchQuery = searchQuery
                return@map item
            }
            return DigitalHomePageSearchNewModel(
                isFromAutoComplete = false,
                searchQuery = searchQuery,
                listSearchResult = filteredData
            )
        }
        throw MessageErrorException("Incorrect data model")
    }
}