package com.tokopedia.inbox.universalinbox.stub.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.stub.data.response.ResponseStub
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.test.application.graphql.GqlMockUtil
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return getResponseFromQuery(requests.first().query)
    }

    private fun getResponseFromQuery(query: String): GraphqlResponse {
        return when {
            query.contains(GqlResponseStub.counterResponse.query) -> {
                shouldThrow(GqlResponseStub.counterResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.counterResponse.responseObject
                )
            }
            query.contains(GqlResponseStub.widgetMetaResponse.query) -> {
                shouldThrow(GqlResponseStub.widgetMetaResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.widgetMetaResponse.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
