package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R

class RightChatMessageViewHolder(itemView: View?, private val listener: ChatLinkHandlerListener)
    : ChatMessageViewHolder(itemView, listener) {

    override fun bind(viewModel: MessageViewModel?) {
        if (viewModel == null) return
        super.bind(viewModel)
        bindReadStatus(viewModel)
    }

    private fun bindReadStatus(chat: MessageViewModel) {
        val imageResource: Int = when {
            chat.isDummy -> com.tokopedia.chat_common.R.drawable.ic_chat_pending
            chat.isRead -> com.tokopedia.chat_common.R.drawable.ic_chat_read
            else -> com.tokopedia.chat_common.R.drawable.ic_chat_unread
        }

        val readStatus = MethodChecker.getDrawable(itemView.context, imageResource)
        fxChat?.changeReadStatus(readStatus)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right
    }
}