package com.tokopedia.gopay.kyc.domain.usecase

import com.tokopedia.gopay.kyc.domain.GQL_CHECK_KYC_STATUS
import com.tokopedia.gopay.kyc.domain.data.CheckKycStatusGqlResponse
import com.tokopedia.gopay.kyc.domain.data.KycStatusResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("CheckKycStatusQuery", GQL_CHECK_KYC_STATUS)
class CheckKycStatusUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<CheckKycStatusGqlResponse>(graphqlRepository) {

    fun checkKycStatus(onSuccess: (KycStatusResponse) -> Unit, onError: (Throwable) -> Unit) {
        this.setTypeClass(CheckKycStatusGqlResponse::class.java)
        this.setRequestParams(getRequestParams())
        this.setGraphqlQuery(CheckKycStatusQuery.GQL_QUERY)
        this.execute(
            { result -> onSuccess(result.kycStatusResponse) },
            { error -> onError(error) }
        )
    }

    private fun getRequestParams() = mapOf(PARTNER_CODE_KEY to PARTNER_CODE_PEMUDA)

    companion object {
        const val PARTNER_CODE_KEY = "partnerCode"
        const val PARTNER_CODE_PEMUDA = "PEMUDA"
    }
}