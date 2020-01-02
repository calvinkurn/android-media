package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.v2.home.data.query.PlayCardQuery
import com.tokopedia.v2.home.model.pojo.home.PlayCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlayRemoteDataSource @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) {
    suspend fun getPlayCard(): Flow<PlayCard> = flow {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val gqlRecommendationRequest = GraphqlRequest(
                PlayCardQuery.getQuery(),
                PlayCard::class.java
        )

        val response = graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        val errors = response.getError(PlayCard::class.java)
        if(errors.isNotEmpty()){
            error(errors.first().message)
        } else {
            emit(response.getSuccessData())
        }
    }
}