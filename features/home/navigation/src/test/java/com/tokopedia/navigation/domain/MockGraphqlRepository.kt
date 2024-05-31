package com.tokopedia.navigation.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type

class MockGraphqlRepository : GraphqlRepository {

    internal val mapOfResponse = mutableMapOf<Type, Any>()

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return GraphqlResponse(
            mapOfResponse,
            emptyMap(),
            false
        )
    }

    internal inline fun <reified T : Any> addResponse(response: T) {
        mapOfResponse[T::class.java] = response
    }
}
