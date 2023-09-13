package com.tokopedia.common.stub

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.graphql.GqlQueryParser
import com.tokopedia.universal_sharing.model.ImageGeneratorModel
import com.tokopedia.universal_sharing.model.ImagePolicyResponse
import com.tokopedia.universal_sharing.test.R as universal_sharingtestR
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import timber.log.Timber

class FakeGraphqlRepository : GraphqlRepository {

    var mockParam = MockParam.NO_PARAM

    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponse {
        when (GqlQueryParser.parse(requests).first()) {
            "generateAffiliateLinkEligibility" -> {
                return if (mockParam === MockParam.NOT_REGISTERED) {
                    GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(universal_sharingtestR.raw.pdp_ticker_register_affiliate)
                } else if (mockParam === MockParam.ELIGIBLE_COMMISSION) {
                    GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(universal_sharingtestR.raw.pdp_eligible_affiliate)
                } else {
                    GqlMockUtil.createSuccessResponse<GenerateAffiliateLinkEligibility.Response>(universal_sharingtestR.raw.pdp_not_eligible_affiliate)
                }
            }
            "imagenerator_generate_image" -> {
                return GqlMockUtil.createSuccessResponse<ImageGeneratorModel.Response>(universal_sharingtestR.raw.imageneratorgenerateimage)
            }
            "imagenerator_policy" -> {
                return GqlMockUtil.createSuccessResponse<ImagePolicyResponse>(universal_sharingtestR.raw.imagenerator_policy)
            }
            else -> {
                throw Exception("request empty")
            }
        }
    }

    enum class MockParam {
        NO_PARAM,
        ELIGIBLE_COMMISSION,
        NOT_ELIGIBLE_COMMISSION,
        NOT_REGISTERED
    }
}
