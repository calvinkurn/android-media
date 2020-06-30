package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekeningPremiumResponse
import javax.inject.Inject
import javax.inject.Named

class GqlRekeningPremiumDataUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_QUERY_GET_REKENING_PREMIUM) val query: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GqlRekeningPremiumResponse>(graphqlRepository) {

    suspend fun getRekeningPremiumData(): Result<GqlRekeningPremiumResponse> {
        this.setTypeClass(GqlRekeningPremiumResponse::class.java)
        this.setGraphqlQuery(query)
        return this.executeUseCase()
    }
}