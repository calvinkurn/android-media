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

class GraphqlRepositoryStub @Inject constructor(
    private val gqlResponseStub: GqlResponseStub
) : GraphqlRepository {

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
                    gqlResponseStub.notificationDetailResponse.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationDetailResponse)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationDetailResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    gqlResponseStub.notificationFilterResponse.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationFilterResponse)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationFilterResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    gqlResponseStub.notificationOrderListResponse.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationOrderListResponse)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationOrderListResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    gqlResponseStub.notificationCounterResponse.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationCounterResponse)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationCounterResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    gqlResponseStub.notificationRecommendation.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationRecommendation)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationRecommendation.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    gqlResponseStub.notificationMarkAsSeen.query
                ) -> {
                shouldThrow(gqlResponseStub.notificationMarkAsSeen)
                GqlMockUtil.createSuccessResponse(
                    gqlResponseStub.notificationMarkAsSeen.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
