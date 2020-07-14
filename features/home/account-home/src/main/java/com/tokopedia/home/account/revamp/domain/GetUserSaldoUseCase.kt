package com.tokopedia.home.account.revamp.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.navigation_common.model.SaldoModel
import javax.inject.Inject

class GetUserSaldoUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<SaldoModel>
) {
    fun getUserSaldo(
            onSuccess: (SaldoModel) -> Unit,
            onError: (Throwable) -> Unit,
            rawQuery: String
    ) {
        gqlUseCase.apply {
            setTypeClass(SaldoModel::class.java)
            setGraphqlQuery(rawQuery)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    fun cancelJobs() {
        gqlUseCase.cancelJobs()
    }
}