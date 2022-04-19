package com.tokopedia.gopay.kyc.domain.usecase

import com.tokopedia.gopay.kyc.domain.GQL_INITIATE_KYC
import com.tokopedia.gopay.kyc.domain.data.InitiateKycGqlResponse
import com.tokopedia.gopay.kyc.domain.data.InitiateKycResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("InitiateKycQuery", GQL_INITIATE_KYC)
class InitiateKycUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<InitiateKycGqlResponse>(graphqlRepository) {

    fun initiateGoPayKyc(onSuccess: (InitiateKycResponse) -> Unit, onError: (Throwable) -> Unit) {
        this.setTypeClass(InitiateKycGqlResponse::class.java)
        this.setRequestParams(getRequestParams())
        this.setGraphqlQuery(InitiateKycQuery.GQL_QUERY)
        this.execute(
            { result -> onSuccess(result.initiateKycResponse) },
            { error -> onError(error) }
        )
    }

    private fun getRequestParams() = mapOf(
        KYC_TYPE_KEY to KYC_TYPE_KTP,
        PARTNER_CODE_KEY to PARTNER_CODE_PEMUDA
    )

    companion object {
        const val PARTNER_CODE_KEY = "partnerCode"
        const val KYC_TYPE_KEY = "kycType"
        const val KYC_TYPE_KTP = "KTP"
        const val PARTNER_CODE_PEMUDA = "PEMUDA"
    }
}