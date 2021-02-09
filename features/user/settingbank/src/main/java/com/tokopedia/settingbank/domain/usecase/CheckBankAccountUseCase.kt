package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_CHECK_ACCOUNT_NUMBER
import com.tokopedia.settingbank.domain.model.CheckAccountResponse
import com.tokopedia.settingbank.view.viewState.AccountCheckState
import com.tokopedia.settingbank.view.viewState.OnAccountCheckSuccess
import com.tokopedia.settingbank.view.viewState.OnCheckAccountError
import com.tokopedia.settingbank.view.viewState.OnErrorInAccountNumber
import javax.inject.Inject


@GqlQuery("GQLCheckBankAccount", GQL_CHECK_ACCOUNT_NUMBER)
class CheckBankAccountUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<CheckAccountResponse>(graphqlRepository) {


    private val KEY_PARAM_ACCOUNT_NUMBER = "accountNumber"
    private val KEY_PARAM_BANK_ID = "bankId"

    var isRunning = false

    fun checkBankAccount(bankId: Long, accountNumber: String?,
                         onResult: (AccountCheckState) -> Unit) {
        if (isRunning)
            return
        isRunning = true
        setTypeClass(CheckAccountResponse::class.java)
        setRequestParams(getAccountCheckParams(bankId, accountNumber))
        setGraphqlQuery(GQLCheckBankAccount.GQL_QUERY)
        execute({
            if (it.checkAccountNumber.successCode == 200) {
                onResult(OnAccountCheckSuccess(it.checkAccountNumber.accountHolderName))
            } else onResult(OnErrorInAccountNumber(it.checkAccountNumber.message))
            isRunning = false
        }, {
            isRunning = false
            onResult(OnCheckAccountError(it))
        })
    }


    private fun getAccountCheckParams(bankId: Long, accountNumber: String?) = mapOf(KEY_PARAM_ACCOUNT_NUMBER to accountNumber,
            KEY_PARAM_BANK_ID to bankId)
}