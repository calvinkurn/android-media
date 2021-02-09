package com.tokopedia.settingbank.view.viewState

sealed class TextWatcherState
data class OnTextChanged(val isCheckEnable: Boolean,
                         val clearAccountHolderName: Boolean,
                         val isAddBankButtonEnable: Boolean) : TextWatcherState()

object OnNOBankSelected : TextWatcherState()


sealed class AccountNameValidationResult
data class OnAccountNameValidated(val name: String) : AccountNameValidationResult()
 object OnAccountValidationFailed : AccountNameValidationResult()


