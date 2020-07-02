package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant.GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL
import com.tokopedia.withdraw.saldowithdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlGetValidatePopUpResponse
import javax.inject.Inject
import javax.inject.Named

class GQLValidateWithdrawalUseCase @Inject constructor(
        @Named(GQL_QUERY_VALIDATE_POP_UP_WITHDRAWAL) val query: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GqlGetValidatePopUpResponse>(graphqlRepository) {


    suspend fun getValidatePopUpData(bankAccount: BankAccount): Result<GqlGetValidatePopUpResponse> {
        this.setTypeClass(GqlGetValidatePopUpResponse::class.java)
        this.setGraphqlQuery(query)
        this.setRequestParams(getRequestParams(bankAccount))
        return executeUseCase()
    }

    private fun getRequestParams(bankAccount: BankAccount): Map<String, Any> {
        return mapOf<String, Any>(
                BANK_ID to bankAccount.bankID.toString(),
                LANGUAGE to LANGUAGE_ID
        )
    }

    companion object {
        const val BANK_ID = "bankID"
        const val LANGUAGE = "language"
        const val LANGUAGE_ID = "ID"
    }

}