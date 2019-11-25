package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.data.query.HomeQuery
import com.tokopedia.home.beranda.domain.model.HomeData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Named

class HomeRemoteDataSource(
        private val graphqlRepository: GraphqlRepository,
        @Named("dispatchersIO") private val dispatchers: CoroutineDispatcher
) {
    suspend fun getHomeData(): GraphqlResponse = withContext(dispatchers) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                HomeQuery.getQuery(),
                HomeData::class.java
        )

        graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
    }
}