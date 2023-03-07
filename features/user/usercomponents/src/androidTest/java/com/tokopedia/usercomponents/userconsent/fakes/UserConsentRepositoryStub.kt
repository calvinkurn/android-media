package com.tokopedia.usercomponents.userconsent.fakes

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.usercomponents.userconsent.fakes.UserConsentUiTestType.*
import javax.inject.Inject

class UserConsentRepositoryStub @Inject constructor() : GraphqlRepository {

    private var testType: UserConsentUiTestType = TNC_SINGLE_MANDATORY

    fun setTestType(testType: UserConsentUiTestType) {
        this.testType = testType
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val response = when(testType) {
            TNC_SINGLE_OPTIONAL -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCSingleOptional()
                )
            }
            TNC_SINGLE_MANDATORY -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCSingleMandatory()
                )
            }
            TNC_SINGLE_MANDATORY_HIDE_CONSENT -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCSingleMandatoryNotNeedConsent()
                )
            }
            TNC_POLICY_SINGLE_OPTIONAL -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCPolicySingleOptional()
                )
            }
            TNC_POLICY_SINGLE_MANDATORY -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCPolicySingleMandatory()
                )
            }
            TNC_MULTIPLE_SOME_ARE_OPTIONAL -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCMultipleOptional()
                )
            }
            TNC_POLICY_MULTIPLE_SOME_ARE_OPTIONAL -> {
                GqlMockUtil.createSuccessResponse(
                    FakeGetCollectionResponse.collectionTnCPolicyMultipleOptional()
                )
            }
        }

        return response
    }
}
