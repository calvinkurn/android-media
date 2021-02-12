package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_GET_USER_BANK_ACCOUNT_LIST
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.BankAccountListResponse
import javax.inject.Inject


@GqlQuery("GQLUserBankAccountList", GQL_GET_USER_BANK_ACCOUNT_LIST)
class GetUserBankAccountUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<BankAccountListResponse>(graphqlRepository) {

    fun getUserBankAccountList(onSuccessBankListSuccess: (List<BankAccount>, Boolean) -> Unit,
                               onFail: (Throwable) -> Unit) {
        setTypeClass(BankAccountListResponse::class.java)
        setGraphqlQuery(GQLUserBankAccountList.GQL_QUERY)
        execute({
            val bankAccountList = it.getBankAccount.data.bankAccount
            val userInfo = it.getBankAccount.data.userInfo
            bankAccountList?.let { bankAccountList ->
                if (bankAccountList.isNotEmpty()) {
                    onSuccessBankListSuccess(bankAccountList, userInfo.isVerified)
                } else {
                    onSuccessBankListSuccess(listOf(), true)
                }
            } ?: run {
                onSuccessBankListSuccess(listOf(), true)
            }
        }, {
            onFail(it)
        })
    }


}


