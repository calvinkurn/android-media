package com.tokopedia.topchat.chatlist.view.uistate

data class TopChatListErrorUiState(
    val error: Pair<Throwable, String>? = null
)
