package com.tokopedia.withdraw.auto_withdrawal.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GqlGetBankDataResponse
import com.tokopedia.withdraw.auto_withdrawal.domain.query.GQL_GET_BANK_ACCOUNT
import javax.inject.Inject

class GQLBankAccountListUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlGetBankDataResponse>(graphqlRepository) {

    fun getBankAccountList(onSuccess: (GqlGetBankDataResponse) -> Unit,
                           onError: (Throwable) -> Unit) {
        this.setTypeClass(GqlGetBankDataResponse::class.java)
        this.setGraphqlQuery(GQL_GET_BANK_ACCOUNT)
        this.execute(
                { result -> onSuccess(result) },
                { error -> onError(error) }
        )
    }
}