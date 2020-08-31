package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout

abstract class ChatMessageViewHolder(itemView: View?, private val listener: ChatLinkHandlerListener)
    : BaseChatViewHolder<MessageViewModel>(itemView) {

    open val fxChat: FlexBoxChatLayout? = itemView?.findViewById(R.id.fxChat)

    override fun bind(message: MessageViewModel) {
        verifyReplyTime(message)
        bindChatMessage(message)
        bindHour(message)
        bindClick()
    }

    protected fun bindClick() {
        itemView.setOnClickListener { v -> KeyboardHandler.DropKeyboard(itemView.context, itemView) }
    }

    protected fun bindChatMessage(chat: MessageViewModel) {
        val movementMethod = ChatLinkHandlerMovementMethod(listener)
        val htmlMessage = MethodChecker.fromHtml(chat.message)
        fxChat?.setMessage(htmlMessage)
        fxChat?.setMovementMethod(movementMethod)
    }

    protected fun bindHour(viewModel: MessageViewModel) {
        val hourTime = getHourTime(viewModel.replyTime)
        fxChat?.setHourTime(hourTime)
    }

    private fun verifyReplyTime(chat: MessageViewModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }
}