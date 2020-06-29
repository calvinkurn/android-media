package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener

class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val commonListener: CommonViewHolderListener
) : ChatMessageViewHolder(itemView, listener) {

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindMessageInfo(message)
    }

    private fun bindMessageInfo(message: MessageViewModel) {
        if (
                message.source == BaseChatViewModel.SOURCE_AUTO_REPLY &&
                !message.isSender &&
                !commonListener.isSeller()
        ) {
            fxChat?.showInfo()
        } else {
            fxChat?.hideInfo()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_left
    }
}