package com.tokopedia.gopay.kyc.domain.usecase

import com.tokopedia.gopay.kyc.domain.GQL_SUBMIT_KYC
import com.tokopedia.gopay.kyc.domain.data.SubmitKycGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("SubmitKycQuery", GQL_SUBMIT_KYC)
class SubmitKycUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitKycGqlResponse>(graphqlRepository) {

    fun submitKyc(
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit,
        kycRequestId: String
    ) {
        this.setTypeClass(SubmitKycGqlResponse::class.java)
        this.setRequestParams(getRequestParams(kycRequestId))
        this.setGraphqlQuery(SubmitKycQuery.GQL_QUERY)
        this.execute(
            { result -> onSuccess(result.submitKycResponse.code) },
            { error -> onError(error) }
        )
    }

    private fun getRequestParams(kycRequestId: String) = mapOf(
        KYC_REQUEST_ID to kycRequestId,
        PARTNER_CODE_KEY to PARTNER_CODE_PEMUDA
    )

    companion object {
        const val PARTNER_CODE_KEY = "partnerCode"
        const val KYC_REQUEST_ID = "kycRequestID"
        const val PARTNER_CODE_PEMUDA = "PEMUDA"
    }
}