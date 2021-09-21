package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder

open class RightChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener
) : CustomChatbotMessageViewHolder(itemView, listener) {

    protected open val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        ChatbotMessageViewHolderBinder.bindChatReadStatus(message, customChatLayout)
        bindBackground(message)
    }

    protected open fun bindBackground(message: MessageViewModel) {
        customChatLayout?.background = bg
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_right
    }
}