package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class TextWatcherState
data class OnTextChanged(val isCheckEnable: Boolean,
                         val clearAccountHolderName: Boolean,
                         val isAddBankButtonEnable: Boolean,
                         val newAccountNumber: String?,
                         val isTextUpdateRequired: Boolean) : TextWatcherState()

object OnNOBankSelected : TextWatcherState()


sealed class AccountNameTextWatcherState
data class OnAccountNameValidated(val name: String) : AccountNameTextWatcherState()
data class OnAccountNameError(val error: String) : AccountNameTextWatcherState()


