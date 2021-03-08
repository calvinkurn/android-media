package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_DELETE_BANK_ACCOUNT
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.DeleteBankAccountResponse
import com.tokopedia.settingbank.util.DeleteBankAccountException
import javax.inject.Inject


@GqlQuery("GQLDeleteBankAccount", GQL_DELETE_BANK_ACCOUNT)
class DeleteBankAccountUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<DeleteBankAccountResponse>(graphqlRepository) {

    private val ACCOUNT_ID = "accID"

    fun deleteBankAccount(bankAccount: BankAccount, onSuccess: (String) -> Unit,
                       onFail: (Throwable) -> Unit) {
        setTypeClass(DeleteBankAccountResponse::class.java)
        setRequestParams(getParams(bankAccount))
        setGraphqlQuery(GQLDeleteBankAccount.GQL_QUERY)
        execute({ it ->
            it.deleteBankAccount.data?.let { deleteBankAccount ->
                when (deleteBankAccount.isSuccess) {
                    true -> onSuccess(deleteBankAccount.messages ?: "")
                    false-> onFail(DeleteBankAccountException(deleteBankAccount.messages ?: ""))
                }
            }?: run {
                onFail(DeleteBankAccountException(""))
            }
        }, {
            onFail(it)
        })
    }


    private fun getParams(bankAccount: BankAccount): Map<String, Any> = mapOf(
            ACCOUNT_ID to bankAccount.accID
    )
}