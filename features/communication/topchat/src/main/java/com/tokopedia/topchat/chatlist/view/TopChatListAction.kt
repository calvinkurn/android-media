package com.tokopedia.topchat.chatlist.view

import com.tokopedia.topchat.chatlist.domain.pojo.TopChatListFilterEnum

sealed class TopChatListAction {
    data class RefreshCounter(val shopId: String) : TopChatListAction()
    data class UpdateCounter(
        val isSellerTab: Boolean,
        val adjustableCounter: Int // Could be + or -
    ) : TopChatListAction()
    data class SetLastVisitedTab(val position: Int) : TopChatListAction()

    data class SetFilter(val filter: TopChatListFilterEnum): TopChatListAction()
}
