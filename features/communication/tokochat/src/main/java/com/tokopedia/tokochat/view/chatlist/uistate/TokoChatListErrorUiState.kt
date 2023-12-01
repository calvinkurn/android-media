package com.tokopedia.tokochat.view.chatlist.uistate

data class TokoChatListErrorUiState(
    val error: Pair<Throwable, String>? = null
)
