package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.home.beranda.data.query.HomeQuery
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Observable

class HomeRemoteDataSource(
        private val graphqlRepository: GraphqlRepository
) {
    suspend fun getHomeData(): GraphqlResponse = withContext(Dispatchers.IO) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                HomeQuery.getQuery(),
                HomeData::class.java
        )

        graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
    }
}