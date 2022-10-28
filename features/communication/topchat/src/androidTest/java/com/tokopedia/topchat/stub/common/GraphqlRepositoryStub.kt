package com.tokopedia.topchat.stub.common

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import kotlinx.coroutines.delay
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor(): GraphqlRepository {

    var mapResult: MutableMap<Type, Any> = hashMapOf()
    var mapGraphqlError: MutableMap<Type, List<GraphqlError>> = hashMapOf()
    var isCached = false
    var delayMs = 0L

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        if (delayMs != 0L) {
            delay(delayMs)
        }
        return GraphqlResponse(mapResult, mapGraphqlError, isCached)
    }

    fun createMapResult(pojo: Type, resultData: Any) {
        mapGraphqlError.clear()
        mapResult[pojo] = resultData
    }

    fun createErrorMapResult(pojo: Type, errorMessage: String) {
        val gqlError = GraphqlError().apply {
            this.message = errorMessage
        }
        mapGraphqlError[pojo] = listOf(gqlError)
    }
}