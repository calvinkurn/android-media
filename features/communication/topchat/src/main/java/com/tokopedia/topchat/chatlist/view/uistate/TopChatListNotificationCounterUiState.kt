package com.tokopedia.topchat.chatlist.view.uistate

data class TopChatListNotificationCounterUiState(
    val unreadSeller: Long = 0,
    val unreadBuyer: Long = 0,
    val isLoading: Boolean = false
)
