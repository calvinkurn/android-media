package com.tokopedia.saldodetails.usecase

import com.tokopedia.saldodetails.data.GqlUseCaseWrapper
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import javax.inject.Inject
import javax.inject.Named

class GetAllTransactionUsecase @Inject
constructor(@Named(GqlQueryModule.DEPOSITE_ALL_TRANSACTION_QUERY) val query: String, private val graphqlUseCase : GqlUseCaseWrapper) {
    private var variables: Map<String, Any>? = null

    // TODO: 27/12/19 remove context from usecase

    fun setRequestVariables(variables: Map<String, Any>) {
        this.variables = variables
    }

    suspend fun execute(variables: Map<String, Any>) : GqlCompleteTransactionResponse {
        return graphqlUseCase.getResponse( GqlCompleteTransactionResponse::class.java, query,
                variables)

    }

}
