package com.tokopedia.shareexperience.stub.data

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shareexperience.data.query.ShareExGetAffiliateEligibilityQuery
import com.tokopedia.shareexperience.data.query.ShareExGetAffiliateLinkQuery
import com.tokopedia.shareexperience.data.query.ShareExGetSharePropertiesQuery
import com.tokopedia.shareexperience.data.query.ShareExImageGeneratorQuery
import com.tokopedia.test.application.graphql.GqlMockUtil
import javax.inject.Inject

class GraphqlRepositoryStub @Inject constructor() : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return getResponseFromQuery(requests.first().query)
    }

    private fun getResponseFromQuery(
        query: String
    ): GraphqlResponse {
        return when {
            query.contains(ShareExGetAffiliateEligibilityQuery.OPERATION_NAME) -> {
                shouldThrow(GqlResponseStub.affiliateEligibilityResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.affiliateEligibilityResponse.responseObject
                )
            }
            query.contains(ShareExGetSharePropertiesQuery.OPERATION_NAME) -> {
                shouldThrow(GqlResponseStub.sharePropertiesResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.sharePropertiesResponse.responseObject
                )
            }
            query.contains(ShareExImageGeneratorQuery.OPERATION_NAME) -> {
                shouldThrow(GqlResponseStub.generatedImageResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.generatedImageResponse.responseObject
                )
            }
            query.contains(ShareExGetAffiliateLinkQuery.OPERATION_NAME) -> {
                shouldThrow(GqlResponseStub.generateAffiliateLinkResponse)
                GqlMockUtil.createSuccessResponse(
                    GqlResponseStub.generateAffiliateLinkResponse.responseObject
                )
            }
            else -> GqlMockUtil.createSuccessResponse(Unit)
        }
    }

    private fun shouldThrow(response: ResponseStub<*>) {
        if (response.isError) throw MessageErrorException("Oops!")
    }
}
