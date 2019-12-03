package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class MakePrimaryAccountResponseState
object OnMakePrimaryRequestStarted : MakePrimaryAccountResponseState()
object OnMakePrimaryRequestEnded : MakePrimaryAccountResponseState()
data class OnMakePrimaryRequestSuccess(val message: String?) : MakePrimaryAccountResponseState()
object OnMakePrimaryRequestNoInternet : MakePrimaryAccountResponseState()
data class OnMakePrimaryRequestFailed(val reason: String?) : MakePrimaryAccountResponseState()