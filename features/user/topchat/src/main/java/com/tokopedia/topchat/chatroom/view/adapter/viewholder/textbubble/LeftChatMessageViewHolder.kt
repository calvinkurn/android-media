package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.common.util.ViewUtil

open class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : ChatMessageViewHolder(itemView, listener, adapterListener) {

    private val bg = ViewUtil.generateBackgroundWithShadow(
            itemView,
            com.tokopedia.unifyprinciples.R.color.Neutral_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.color.topchat_message_shadow,
            R.dimen.dp_2,
            R.dimen.dp_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindMessageInfo(message)
        bindBackground(message)
    }

    private fun bindBackground(message: MessageViewModel) {
        fxChat?.background = bg
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