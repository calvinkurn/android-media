package com.tokopedia.v2.home.data.datasource.remote

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.constant.ConstantKey.RequestKey.USER_ID
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.v2.home.model.pojo.HomeData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRemoteDataSource (
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val homeQuery: String
){
    suspend fun getHomeData(): GraphqlResponse {
        return withContext(Dispatchers.IO){
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val params = mapOf(
                USER_ID to if(userSessionInterface.isLoggedIn) userSessionInterface.userId.toInt() else 0
            )

            val gqlRecommendationRequest = GraphqlRequest(
                    homeQuery,
                    HomeData::class.java,
                    params
            )

            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        }
    }
}