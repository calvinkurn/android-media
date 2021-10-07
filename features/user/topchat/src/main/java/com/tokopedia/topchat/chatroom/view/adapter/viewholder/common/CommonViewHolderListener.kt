package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.topchat.common.analytics.TopChatAnalytics

interface CommonViewHolderListener {
    fun isSeller(): Boolean
    fun getAnalytic(): TopChatAnalytics
    fun showMsgMenu(msg: BaseChatViewModel, text: CharSequence)
}