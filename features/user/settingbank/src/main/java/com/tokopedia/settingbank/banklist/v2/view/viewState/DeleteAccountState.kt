package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class DeleteAccountState
object OnDeleteAccountRequestStarted : DeleteAccountState()
object OnDeleteAccountRequestEnded : DeleteAccountState()
data class OnDeleteAccountRequestSuccess(val message: String?) : DeleteAccountState()
data class OnDeleteAccountRequestFailed(val throwable: Throwable) : DeleteAccountState()