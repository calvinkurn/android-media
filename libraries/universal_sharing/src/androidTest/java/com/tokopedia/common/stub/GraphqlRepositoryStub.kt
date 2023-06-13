package com.tokopedia.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.universal_sharing.test.R
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility

class GraphqlRepositoryStub : GraphqlRepository {

    var mockParam = MockParam.NO_PARAM
    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        when (GqlQueryParser.parse(requests).first()) {
            "generateAffiliateLinkEligibility" -> {
                return if (mockParam === MockParam.ELIGIBLE_COMMISSION) {
                    GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(R.raw.pdp_eligible_affiliate)
                } else {
                    GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(R.raw.pdp_not_eligible_affiliate)
                }
            }
            else -> {
                throw Exception("request empty")
            }
        }
    }

    enum class MockParam {
        NO_PARAM,
        ELIGIBLE_COMMISSION,
        NOT_ELIGIBLE_COMMISSION
    }
}
