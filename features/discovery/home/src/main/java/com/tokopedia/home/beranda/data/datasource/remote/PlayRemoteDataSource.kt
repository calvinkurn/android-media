package com.tokopedia.home.beranda.data.datasource.remote

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.beranda.data.model.PlayLiveDynamicChannelEntity
import com.tokopedia.home.beranda.data.query.PlayLiveDynamicChannelQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Named

class PlayRemoteDataSource(
        private val graphqlRepository: GraphqlRepository,
        @Named("dispatchersIO") private val dispatchers: CoroutineDispatcher
) {
    suspend fun getPlayData() = withContext(dispatchers) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                PlayLiveDynamicChannelQuery.getQuery(),
                PlayLiveDynamicChannelEntity::class.java
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(PlayLiveDynamicChannelEntity::class.java)
        if(errors.isNotEmpty()){
            error(errors.first().message)
        }
        response.getSuccessData<PlayLiveDynamicChannelEntity>()
    }
}