package com.tokopedia.withdraw.saldowithdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.withdraw.saldowithdrawal.domain.helper.WithdrawalDomainConstant
import com.tokopedia.withdraw.saldowithdrawal.domain.model.GqlRekPreTncResponse
import javax.inject.Inject
import javax.inject.Named

class JoinRekeningTNCUseCase @Inject constructor(
        @Named(WithdrawalDomainConstant.GQL_JOIN_REKE_PREM_TNC_QUERY) private val query: String,
        graphqlRepository: GraphqlRepository)
    : SaldoGQLUseCase<GqlRekPreTncResponse>(graphqlRepository) {


    suspend fun loadJoinRekeningTermsCondition(programID: Int): Result<GqlRekPreTncResponse> {
        this.setTypeClass(GqlRekPreTncResponse::class.java)
        this.setGraphqlQuery(query)
        this.setRequestParams(getRequestParams(programID))
        return executeUseCase()
    }

    private fun getRequestParams(programID: Int): Map<String, Any> {
        return mapOf(
                PARAM_IS_QUIT to false,
                PARAM_PROGRAM_ID to programID
        )
    }

    companion object {
        const val PARAM_PROGRAM_ID = "programID"
        const val PARAM_IS_QUIT = "isQuit"
    }

}