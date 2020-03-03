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
        bindChatReadStatus(viewModel)
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right
    }
}