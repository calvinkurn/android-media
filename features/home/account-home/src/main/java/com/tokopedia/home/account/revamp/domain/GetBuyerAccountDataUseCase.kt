package com.tokopedia.home.account.revamp.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.home.account.data.model.AccountModel
import javax.inject.Inject

class GetBuyerAccountDataUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<AccountModel>
) {
    fun getBuyerAccount(
        onSuccess: (AccountModel) -> Unit,
        onError: (Throwable) -> Unit,
        rawQuery: String
    ) {
        gqlUseCase.apply {
            setTypeClass(AccountModel::class.java)
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