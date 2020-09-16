package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout

abstract class ChatMessageViewHolder(
        itemView: View?,
        private val listener: ChatLinkHandlerListener,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<MessageViewModel>(itemView) {

    protected open val fxChat: FlexBoxChatLayout? = itemView?.findViewById(R.id.fxChat)
    protected open val msgContainer: ConstraintLayout? = itemView?.findViewById(R.id.cl_msg_container)
    protected val bottomMarginOpposite: Float = itemView?.context?.resources?.getDimension(
            com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
    ) ?: 0f

    override fun bind(message: MessageViewModel) {
        verifyReplyTime(message)
        bindChatMessage(message)
        bindHour(message)
        bindMargin(message)
        bindClick()
    }

    protected fun bindMargin(message: MessageViewModel) {
        val lp = msgContainer?.layoutParams
        if (lp is RecyclerView.LayoutParams) {
            if (!adapterListener.isNextItemSender(adapterPosition, message.isSender)) {
                msgContainer?.setMargin(0, 0, 0, bottomMarginOpposite.toInt())
            } else {
                msgContainer?.setMargin(0, 0, 0, 0)
            }
        }
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