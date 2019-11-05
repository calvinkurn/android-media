package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.v2.home.model.pojo.HomeData

class HomeRemoteDataSource (
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val homeQuery: String
){
    suspend fun getHomeData(): GraphqlResponse {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                homeQuery,
                HomeData::class.java
        )

        return graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
    }
}