package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_GET_BANK_ACCOUNT
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlGetBankDataResponse
import javax.inject.Inject
import javax.inject.Named

class GetBankListUseCase @Inject constructor(
        @Named(GQL_QUERY_GET_BANK_ACCOUNT) val bankListQuery: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GqlGetBankDataResponse>(graphqlRepository) {

    suspend fun getBankList(): Result<GqlGetBankDataResponse> {
        setGraphqlQuery(bankListQuery)
        setTypeClass(GqlGetBankDataResponse::class.java)
        return executeUseCase()
    }
}