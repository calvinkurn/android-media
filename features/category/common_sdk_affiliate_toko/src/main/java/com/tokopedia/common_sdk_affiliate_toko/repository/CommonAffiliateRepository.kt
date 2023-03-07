package com.tokopedia.common_sdk_affiliate_toko.repository

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject


class CommonAffiliateRepository @Inject constructor(){
    private val graphqlRepository: GraphqlRepository by lazy { GraphqlInteractor.getInstance().graphqlRepository }
    suspend fun <T : Any> getGQLData(gqlQuery: String,
                                     gqlResponseType: Class<T>,
                                     gqlParams: Map<String, Any>,
                                     cacheType: CacheType = CacheType.CLOUD_THEN_CACHE): T {
        try {
            val gqlUseCase = GraphqlUseCase<T>(graphqlRepository)
            gqlUseCase.setTypeClass(gqlResponseType)
            gqlUseCase.setGraphqlQuery(gqlQuery)
            gqlUseCase.setRequestParams(gqlParams)

            gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(cacheType).build())
            return gqlUseCase.executeOnBackground()
        } catch (t: Throwable) {
            throw t
        }
    }
}