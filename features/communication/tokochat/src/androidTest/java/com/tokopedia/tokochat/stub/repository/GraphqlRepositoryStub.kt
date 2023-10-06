package com.tokopedia.tokochat.stub.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatBackgroundResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatFirstTickerResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getNeedConsentResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getTkpdOrderIdResponse
import com.tokopedia.tokochat.stub.domain.response.ResponseStub
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
            query.contains(chatBackgroundResponse.query) -> {
                shouldThrow(chatBackgroundResponse)
                GqlMockUtil.createSuccessResponse(
                    chatBackgroundResponse.responseObject
                )
            }
            query.contains(chatFirstTickerResponse.query) -> {
                shouldThrow(chatFirstTickerResponse)
                GqlMockUtil.createSuccessResponse(
                    chatFirstTickerResponse.responseObject
                )
            }
            query.contains(chatOrderHistoryResponse.query) -> {
                shouldThrow(chatOrderHistoryResponse)
                GqlMockUtil.createSuccessResponse(
                    chatOrderHistoryResponse.responseObject
                )
            }
            query.contains(getNeedConsentResponse.query) -> {
                shouldThrow(getNeedConsentResponse)
                GqlMockUtil.createSuccessResponse(
                    getNeedConsentResponse.responseObject
                )
            }
            query.contains(getTkpdOrderIdResponse.query) -> {
                shouldThrow(getTkpdOrderIdResponse)
                GqlMockUtil.createSuccessResponse(
                    getTkpdOrderIdResponse.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
