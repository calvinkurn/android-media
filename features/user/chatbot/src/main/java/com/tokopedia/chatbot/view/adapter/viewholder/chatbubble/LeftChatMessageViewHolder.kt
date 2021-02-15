package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil

class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
) : CustomChatbotMessageViewHolder(itemView, listener) {

    private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindBackground(message)
    }

    private fun bindBackground(message: MessageViewModel) {
        customChatLayout?.background = bg
    }

    private fun bindMessageInfo(message: MessageViewModel) {
        if (!message.isSender) {
            customChatLayout?.showInfo()
        } else {
            customChatLayout?.hideInfo()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_left
    }
}