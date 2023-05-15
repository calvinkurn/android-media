package com.tokopedia.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser

class GraphqlRepositoryStub : GraphqlRepository {
    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        requests.forEach { request ->
            when (GqlQueryParser.parse(request.query).first()) {
                "generateAffiliateLinkEligibility" -> {
                    return GqlMockUtil.createSuccessResponse()
                }
            }
        }
    }
}
