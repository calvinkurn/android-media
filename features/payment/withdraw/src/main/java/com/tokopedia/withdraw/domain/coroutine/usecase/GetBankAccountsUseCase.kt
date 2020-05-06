package com.tokopedia.withdraw.domain.coroutine.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.withdraw.constant.WithdrawalDomainConstant.GQL_QUERY_GET_BANK_ACCOUNT
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.GqlBankListResponse
import com.tokopedia.withdraw.domain.model.GqlGetBankDataResponse
import javax.inject.Inject
import javax.inject.Named

class GetBankAccountsUseCase @Inject constructor(
        @Named(GQL_QUERY_GET_BANK_ACCOUNT) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GqlGetBankDataResponse>(graphqlRepository) {

    fun getBankAccountList(onSuccess: (bankAccountList: ArrayList<BankAccount>) -> Unit,
                           onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(GqlGetBankDataResponse::class.java)
            this.setGraphqlQuery(query)
            this.execute(
                    {
                        onSuccess(it.bankAccount.bankAccountList)
                    }, { error ->
                onError(error)
            })
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}