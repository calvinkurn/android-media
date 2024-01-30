package com.tokopedia.shareexperience.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
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
//            (
//                GqlQueryParser.parse(query).first() ==
//                    GqlResponseStub.topAdsHeadlineResponse.query
//                ) -> {
//                shouldThrow(GqlResponseStub.topAdsHeadlineResponse)
//                GqlMockUtil.createSuccessResponse(
//                    GqlResponseStub.topAdsHeadlineResponse.responseObject
//                )
//            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
