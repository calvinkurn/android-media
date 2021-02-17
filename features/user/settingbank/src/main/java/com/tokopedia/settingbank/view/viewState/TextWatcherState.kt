package com.tokopedia.settingbank.view.viewState

sealed class ValidateAccountNumberState
data class ValidateAccountNumberSuccess(val isCheckEnable: Boolean,
                         val isAddBankButtonEnable: Boolean) : ValidateAccountNumberState()

object OnNOBankSelected : ValidateAccountNumberState()


sealed class AccountNameValidationResult
data class OnAccountNameValidated(val name: String) : AccountNameValidationResult()
 object OnAccountValidationFailed : AccountNameValidationResult()


