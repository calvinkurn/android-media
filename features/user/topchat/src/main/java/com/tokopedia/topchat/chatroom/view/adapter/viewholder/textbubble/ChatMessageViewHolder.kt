package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import kotlinx.android.synthetic.main.item_topchat_chat_left.view.*

abstract class ChatMessageViewHolder(itemView: View?) : BaseChatViewHolder<MessageViewModel>(itemView) {

    override fun bind(viewModel: MessageViewModel?) {
        if (viewModel == null) return
        bindChatMessage(viewModel)
        bindHour(viewModel)
    }

    protected fun bindChatMessage(chat: MessageViewModel) {
        itemView.fxChat?.setMessage(chat.message)
    }

    protected fun bindHour(viewModel: MessageViewModel) {
        val hourTime = getHourTime(viewModel.replyTime)
        itemView.fxChat?.setHourTime(hourTime)
    }

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }
}