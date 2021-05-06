package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_BANK_KYC_CHECK
import com.tokopedia.settingbank.domain.model.KYCCheckResponse
import com.tokopedia.settingbank.domain.model.KYCInfo
import javax.inject.Inject

@GqlQuery("GQLBankKycCheck", GQL_BANK_KYC_CHECK)
class KyCInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<KYCCheckResponse>(graphqlRepository) {

    var isCheckingKYC = false

    init {
        setGraphqlQuery(GQLBankKycCheck.GQL_QUERY)
        setTypeClass(KYCCheckResponse::class.java)
    }

    fun getKYCCheckInfo(onSuccess: (KYCInfo) -> Unit, onError: (Throwable) -> Unit) {
        if (isCheckingKYC)
            return
        isCheckingKYC = true
        execute({
            onSuccess(it.kycInfo)
            isCheckingKYC = false
        }, {
            onError(it)
            isCheckingKYC = false
        })
    }

}
