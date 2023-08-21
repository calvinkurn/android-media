package com.tokopedia.notifcenter.stub.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.stub.data.response.ResponseStub
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return getResponseFromQuery(
            requests.first().query,
            requests.first().variables
        )
    }

    private fun getResponseFromQuery(
        query: String,
        variables: Map<String, Any?>?
    ): GraphqlResponse {
        return when {
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationDetailResponse.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationDetailResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationDetailResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationFilterResponse.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationFilterResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationFilterResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationOrderListResponse.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationOrderListResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationOrderListResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationCounterResponse.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationCounterResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationCounterResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationRecommendation.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationRecommendation)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationRecommendation.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.notificationMarkAsSeen.query
                ) -> {
                shouldThrow(GqlResponseStub.notificationMarkAsSeen)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.notificationMarkAsSeen.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
