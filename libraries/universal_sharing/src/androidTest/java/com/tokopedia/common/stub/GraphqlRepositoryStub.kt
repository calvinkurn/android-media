package com.tokopedia.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.test.R

class GraphqlRepositoryStub : GraphqlRepository {
    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        when (GqlQueryParser.parse(requests).first()) {
            "generateAffiliateLinkEligibility" -> {
                println("masuk sini")
                return GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(R.raw.pdp_eligible_affiliate)
            }
            else -> {
                throw Exception("request empty")
            }
        }
    }
}
