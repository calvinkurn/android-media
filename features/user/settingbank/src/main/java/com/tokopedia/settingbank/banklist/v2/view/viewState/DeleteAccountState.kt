package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class DeleteAccountState
object OnDeleteAccountRequestStarted : DeleteAccountState()
object OnDeleteAccountRequestEnded : DeleteAccountState()
data class OnDeleteAccountRequestSuccess(val message: String?) : DeleteAccountState()
object OnDeleteAccountNoInternet : DeleteAccountState()
data class OnDeleteAccountRequestFailed(val reason: String?) : DeleteAccountState()