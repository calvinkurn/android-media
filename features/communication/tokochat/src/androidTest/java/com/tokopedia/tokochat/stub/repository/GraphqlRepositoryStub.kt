package com.tokopedia.tokochat.stub.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatBackgroundResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatFirstTickerResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryLogisticResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryTokoFoodResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getNeedConsentResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getTkpdOrderIdResponse
import com.tokopedia.tokochat.stub.domain.response.ResponseStub
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return getResponseFromQuery(requests.first().query, requests.first().variables)
    }

    private fun getResponseFromQuery(
        query: String,
        variables: Map<String, Any?>?
    ): GraphqlResponse {
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
            (
                query.contains(chatOrderHistoryTokoFoodResponse.query) &&
                    variables?.get("serviceType") == "tokofood"
                ) -> {
                shouldThrow(chatOrderHistoryTokoFoodResponse)
                GqlMockUtil.createSuccessResponse(
                    chatOrderHistoryTokoFoodResponse.responseObject
                )
            }
            (
                query.contains(chatOrderHistoryLogisticResponse.query) &&
                    variables?.get("serviceType") == "logistic"
                ) -> {
                shouldThrow(chatOrderHistoryLogisticResponse)
                GqlMockUtil.createSuccessResponse(
                    chatOrderHistoryLogisticResponse.responseObject
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
