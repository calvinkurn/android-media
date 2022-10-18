package com.tokopedia.home_account.consentwithdrawal.fakes

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_account.consentwithdrawal.fakes.data.FakeGetConsentGroupListResponse
import com.tokopedia.home_account.consentwithdrawal.fakes.data.FakeGetPurposeListByGroupResponse
import com.tokopedia.home_account.consentwithdrawal.fakes.data.FakeSubmitConsentPreferenceResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import javax.inject.Inject

enum class ConsentWithdrawalTestCase {
    LOAD_CONSENT_GROUP_LIST,
    LOAD_CONSENT_BY_GROUP_ID,
    LOAD_CONSENT_BY_GROUP_ID_WITH_OPT_OUT_LIST,
    SUBMIT_CONSENT_PREFERENCE,
}

class ConsentWithdrawalRepositoryStub @Inject constructor() : GraphqlRepository {

    private var testCase: ConsentWithdrawalTestCase? = null
    fun setTestCase(testCase: ConsentWithdrawalTestCase) {
        this.testCase = testCase
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        return when (testCase) {
            ConsentWithdrawalTestCase.LOAD_CONSENT_GROUP_LIST -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetConsentGroupListResponse.getResponse()
                )
            }
            ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetPurposeListByGroupResponse.getResponse()
                )
            }
            ConsentWithdrawalTestCase.LOAD_CONSENT_BY_GROUP_ID_WITH_OPT_OUT_LIST -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetPurposeListByGroupResponse.getResponseWithOptOutList()
                )
            }
            ConsentWithdrawalTestCase.SUBMIT_CONSENT_PREFERENCE -> {
                GqlMockUtil.createSuccessResponse(
                    FakeSubmitConsentPreferenceResponse.getResponse()
                )
            }
            else -> throw Exception("Bad Request")
        }
    }

}
