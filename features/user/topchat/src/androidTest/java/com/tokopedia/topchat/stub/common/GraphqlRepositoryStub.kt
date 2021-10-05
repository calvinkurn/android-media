package com.tokopedia.topchat.stub.common

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub<T: Any> @Inject constructor(): GraphqlRepository {

    var mapResult: MutableMap<Type, Any> = hashMapOf()
    var mapGraphqlError: MutableMap<Type, MutableList<GraphqlError>> = hashMapOf()
    var isCached = false

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return GraphqlResponse(mapResult, mapGraphqlError, isCached)
    }

    fun createMapResult(useCase: T, resultData: Any) {
        mapResult[useCase::class.java] = resultData
    }

    fun createErrorMapResult(useCase: T, errorMessage: String) {
        mapResult[useCase::class.java] = GraphqlError().apply {
            this.message = errorMessage
        }
    }
}