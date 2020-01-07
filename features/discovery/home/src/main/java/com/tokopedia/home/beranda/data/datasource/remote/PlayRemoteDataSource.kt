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
    companion object{
        private const val PARAM_PAGE = "page"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_DEVICE = "device"
        private const val DEFAULT_SOURCE = "home-play"
        private const val DEFAULT_LIMIT = 1
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_DEVICE = "android"
    }
    suspend fun getPlayData(
            source: String = DEFAULT_SOURCE, page: Int = DEFAULT_PAGE, limit: Int = DEFAULT_LIMIT
    ) = withContext(dispatchers) {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val params = mapOf(
                PARAM_SOURCE to source,
                PARAM_PAGE to page,
                PARAM_LIMIT to limit,
                PARAM_DEVICE to DEFAULT_DEVICE
        )
        val gqlRecommendationRequest = GraphqlRequest(
                PlayLiveDynamicChannelQuery.getQuery(),
                PlayLiveDynamicChannelEntity::class.java,
                params
        )
        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(PlayLiveDynamicChannelEntity::class.java)
        if(errors?.isNotEmpty() == true){
            error(errors.first().message)
        }
        response.getSuccessData<PlayLiveDynamicChannelEntity>()
    }
}