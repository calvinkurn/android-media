package com.tokopedia.topchat.chatlist.view

sealed class TopChatListAction {
    data class RefreshCounter(val shopId: String) : TopChatListAction()
    data class UpdateCounter(
        val isSellerTab: Boolean,
        val adjustableCounter: Int // Could be + or -
    ) : TopChatListAction()
}
