package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.v2.home.model.pojo.HomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRemoteDataSource (
        private val graphqlRepository: GraphqlRepository,
        private val homeQuery: String
){
    suspend fun getHomeData(): GraphqlResponse = withContext(Dispatchers.IO) {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                homeQuery,
                HomeData::class.java
        )

        graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
    }

    suspend fun getOldHomeData(): GraphqlResponse = withContext(Dispatchers.IO) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                homeQuery,
                com.tokopedia.home.beranda.domain.model.HomeData::class.java
        )

        graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
    }
}