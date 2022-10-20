package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.topchat.common.analytics.TopChatAnalytics

interface CommonViewHolderListener {
    fun isSeller(): Boolean
    fun getAnalytic(): TopChatAnalytics
    fun showMsgMenu(
        msg: BaseChatUiModel, text: CharSequence, menus: List<Int>
    )
    fun getCommonShopId(): String

    // for read message trackers in bubble chat
    fun impressReadMessageForBubbles(replyId: String)
}
