package com.tokopedia.promotionstarget

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy

class GqlUseCaseWrapper {

    suspend fun <T : Any> getResponse(gqlResponseType: Class<T>, query: String, params: Map<String, Any>): T {
        val graphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
        val gqlUseCase = GraphqlUseCase<T>(graphqlRepository)
                .apply {
                    setTypeClass(gqlResponseType)
                    setGraphqlQuery(query)
                    setRequestParams(params)
                    setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
                }
        return gqlUseCase.executeOnBackground()
    }

}