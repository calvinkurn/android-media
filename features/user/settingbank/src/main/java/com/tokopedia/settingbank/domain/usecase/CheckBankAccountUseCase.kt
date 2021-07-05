package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_CHECK_ACCOUNT_NUMBER
import com.tokopedia.settingbank.domain.model.CheckAccountResponse
import com.tokopedia.settingbank.view.viewState.*
import javax.inject.Inject


@GqlQuery("GQLCheckBankAccount", GQL_CHECK_ACCOUNT_NUMBER)
class CheckBankAccountUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<CheckAccountResponse>(graphqlRepository) {


    private val KEY_PARAM_ACCOUNT_NUMBER = "accountNumber"
    private val KEY_PARAM_BANK_ID = "bankId"
    private val KEY_ACTION = "action"
    private val KEY_EDITED_ACCOUNT_NAME = "editedAccountName"

    private val ACTION_CHECK_ACCOUNT_NAME = "check_account_name"
    private val ACTION_VALIDATE = "validate"

    var isRunning = false

    fun checkAccountNumber(bankId: Long, accountNumber: String?,
                           onSuccess: (CheckAccountNameState) -> Unit,
                           onFail: (Throwable) -> Unit) {
        checkBankAccount(bankId, accountNumber,
                null, ACTION_CHECK_ACCOUNT_NAME, onSuccess, onFail)
    }

    fun validateAccountNumber(bankId: Long, accountNumber: String?,
                              accountHolderName: String?,
                              onSuccess: (CheckAccountNameState) -> Unit,
                              onFail: (Throwable) -> Unit) {
        checkBankAccount(bankId, accountNumber,
                accountHolderName ?: "", ACTION_VALIDATE, onSuccess, onFail)
    }


    private fun checkBankAccount(bankId: Long, accountNumber: String?,
                                 editedAccountName: String?, action: String,
                                 onSuccess: (CheckAccountNameState) -> Unit,
                                 onFail: (Throwable) -> Unit) {
        if (isRunning)
            return
        isRunning = true
        setTypeClass(CheckAccountResponse::class.java)
        setRequestParams(getAccountCheckParams(bankId, accountNumber, editedAccountName, action))
        setGraphqlQuery(GQLCheckBankAccount.GQL_QUERY)
        execute({
            val checkAccountData = it.checkAccountData
            if (checkAccountData.successCode == 200) {
                if (checkAccountData.isValidBankAccount) {
                    if(checkAccountData.allowedToEdit){
                        onSuccess(EditableAccountName(checkAccountData.accountHolderName ?: "",
                                true,
                                checkAccountData.message))
                    }else {
                        if (action == ACTION_VALIDATE) {
                            onSuccess(AccountNameFinalValidationSuccess(editedAccountName
                                    ?: "", ActionValidateAccountName))
                        } else {
                            onSuccess(AccountNameFinalValidationSuccess(checkAccountData.accountHolderName
                                    ?: "",
                                    ActionCheckAccountAccountName))
                        }
                    }
                } else {
                    onSuccess(EditableAccountName(checkAccountData.accountHolderName ?: "",
                            false,
                            checkAccountData.message))
                }
            } else {
                onSuccess(AccountNameCheckError(checkAccountData.accountHolderName ?: "",
                        checkAccountData.message))
            }
            isRunning = false
        }, {
            isRunning = false
            onFail(it)
        })
    }


    private fun getAccountCheckParams(bankId: Long, accountNumber: String?,
                                      editedAccountName: String?, action: String): Map<String, Any?> {
        return mutableMapOf(KEY_PARAM_ACCOUNT_NUMBER to accountNumber,
                KEY_PARAM_BANK_ID to bankId,
                KEY_ACTION to action).apply {
            editedAccountName?.let {
                put(KEY_EDITED_ACCOUNT_NAME, editedAccountName)
            }
        }
    }
}