package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R

class LeftChatMessageViewHolder(itemView: View?, private val listener: ChatLinkHandlerListener)
    : ChatMessageViewHolder(itemView, listener) {

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_left
    }
}