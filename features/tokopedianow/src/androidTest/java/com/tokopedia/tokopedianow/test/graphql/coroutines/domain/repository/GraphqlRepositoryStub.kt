package com.tokopedia.tokopedianow.test.graphql.coroutines.domain.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import javax.inject.Inject

/**
 * Stub gql query call based on given query name and response object map
 * @param gqlQueryStubMap map of gql query name and response object
 *
 * Example:
 *
 * mapOf("operationName" to GraphqlResponse)
 *
 * Assign Throwable to response object to stub error response:
 *
 * mapOf("operationName" to Throwable("error message"))
 */
class GraphqlRepositoryStub @Inject constructor(
    private val gqlQueryStubMap: Map<String, Any>
) : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val query = requests.first().query
        val obj = gqlQueryStubMap.entries
            .firstOrNull { query.contains(it.key) }?.value

        return when {
            obj != null && obj is GraphqlResponse -> obj
            obj != null && obj is Throwable -> throw obj
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }
}
