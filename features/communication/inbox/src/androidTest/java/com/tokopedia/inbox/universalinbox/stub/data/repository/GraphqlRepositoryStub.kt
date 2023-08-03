package com.tokopedia.inbox.universalinbox.stub.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.stub.data.response.ResponseStub
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WIDGET_PAGE_NAME
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.PARAM_PAGE_NAME
import com.tokopedia.recommendation_widget_common.PARAM_X_SOURCE
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
            (GqlQueryParser.parse(query).first() == GqlResponseStub.counterResponse.query) -> {
                shouldThrow(GqlResponseStub.counterResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.counterResponse.responseObject
                )
            }
            (GqlQueryParser.parse(query).first() == GqlResponseStub.widgetMetaResponse.query) -> {
                shouldThrow(GqlResponseStub.widgetMetaResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.widgetMetaResponse.responseObject
                )
            }
            (
                (
                    GqlQueryParser.parse(query).first() ==
                        GqlResponseStub.productRecommendationResponse.query
                    ) &&
                    variables?.get(PARAM_X_SOURCE) == DEFAULT_VALUE_X_SOURCE
                ) -> {
                shouldThrow(GqlResponseStub.productRecommendationResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.productRecommendationResponse.responseObject
                )
            }
            (
                (
                    GqlQueryParser.parse(query).first() ==
                        GqlResponseStub.prePurchaseProductRecommendationResponse.query
                    ) &&
                    variables?.get(PARAM_PAGE_NAME) == WIDGET_PAGE_NAME
                ) -> {
                shouldThrow(GqlResponseStub.prePurchaseProductRecommendationResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.prePurchaseProductRecommendationResponse.responseObject
                )
            }
            (
                GqlQueryParser.parse(query).first() ==
                    GqlResponseStub.topAdsHeadlineResponse.query
                ) -> {
                shouldThrow(GqlResponseStub.topAdsHeadlineResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.topAdsHeadlineResponse.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
