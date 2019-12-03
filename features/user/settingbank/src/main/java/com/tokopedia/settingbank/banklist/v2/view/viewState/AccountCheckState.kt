package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class AccountCheckState
data class OnErrorInAccountNumber(val errorMessage : String?) : AccountCheckState()
data class OnAccountCheckSuccess(val accountHolderName : String?) : AccountCheckState()
object OnNetworkError : AccountCheckState()
