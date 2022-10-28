package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by furqan on 14/01/2020
 */

class TravelRecentSearchUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TravelRecentSearchModel.Response>(graphqlRepository) {

    suspend fun execute(query: GqlQueryInterface, isFromCloud: Boolean): TravelRecentSearchModel {

        try {
            val cacheType = if (isFromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST
            this.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
            this.setTypeClass(TravelRecentSearchModel.Response::class.java)
            this.setGraphqlQuery(query)

            val travelRencentSearchData = this.executeOnBackground()

            return travelRencentSearchData.response
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

}