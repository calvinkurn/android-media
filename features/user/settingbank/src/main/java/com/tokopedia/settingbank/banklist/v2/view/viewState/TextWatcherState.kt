package com.tokopedia.settingbank.banklist.v2.view.viewState

sealed class TextWatcherState
object OnTextWatcherSuccess : TextWatcherState()
object OnNoBankSelected : TextWatcherState()
data class OnTextWatcherError(val error: String) : TextWatcherState()
