package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class MakePrimaryAccountResponseState
object OnMakePrimaryRequestStarted : MakePrimaryAccountResponseState()
object OnMakePrimaryRequestEnded : MakePrimaryAccountResponseState()
data class OnMakePrimaryRequestSuccess(val message: String?) : MakePrimaryAccountResponseState()
data class OnMakePrimaryRequestError(val throwable: Throwable) : MakePrimaryAccountResponseState()