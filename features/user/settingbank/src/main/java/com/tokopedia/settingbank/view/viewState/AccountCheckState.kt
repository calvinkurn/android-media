package com.tokopedia.settingbank.view.viewState

sealed class AccountCheckState
data class OnErrorInAccountNumber(val errorMessage : String?) : AccountCheckState()
data class OnAccountCheckSuccess(val accountHolderName : String?) : AccountCheckState()
data class OnCheckAccountError(val throwable: Throwable): AccountCheckState()
