package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_WITHDRAWAL_BANNER
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GetWDBannerResponse
import javax.inject.Inject
import javax.inject.Named

class GetWDBannerUseCase @Inject constructor(
        @Named(GQL_QUERY_WITHDRAWAL_BANNER) val query: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GetWDBannerResponse>(graphqlRepository) {

    suspend fun getRekeningProgramBanner(): Result<GetWDBannerResponse> {
        setGraphqlQuery(query)
        setTypeClass(GetWDBannerResponse::class.java)
        return executeUseCase()
    }
}