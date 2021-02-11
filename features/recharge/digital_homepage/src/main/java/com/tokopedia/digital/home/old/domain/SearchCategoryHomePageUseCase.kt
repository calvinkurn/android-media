package com.tokopedia.digital.home.old.domain

import com.tokopedia.digital.home.old.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomePageCategoryDataMapper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException

class SearchCategoryHomePageUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<DigitalHomePageCategoryModel>(graphqlRepository) {

    suspend fun searchCategoryList(rawQuery: String, isLoadFromCloud: Boolean, searchQuery: String): List<DigitalHomePageSearchCategoryModel> {
        setGraphqlQuery(rawQuery)
        setTypeClass(DigitalHomePageCategoryModel::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST).build())

        val rawCategoryData = DigitalHomePageCategoryDataMapper.mapCategoryData(executeOnBackground())
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
            return filteredData
        }
        throw MessageErrorException("Incorrect data model")
    }
}