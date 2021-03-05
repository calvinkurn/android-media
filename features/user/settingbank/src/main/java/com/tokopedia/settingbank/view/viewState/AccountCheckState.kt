package com.tokopedia.settingbank.view.viewState

sealed class CheckAccountNameState
data class EditableAccountName(val accountName: String,
                               val isValidBankAccount : Boolean,
                                val message : String?) : CheckAccountNameState()
data class AccountNameCheckError(val accountName: String,
                                    val message : String?) : CheckAccountNameState()
data class AccountNameFinalValidationSuccess(val accountHolderName : String,
                                             val checkAccountAction: CheckAccountAction) : CheckAccountNameState()


sealed class CheckAccountAction
object ActionValidateAccountName : CheckAccountAction()
object ActionCheckAccountAccountName : CheckAccountAction()
