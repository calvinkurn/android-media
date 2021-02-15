package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.toLongOrZero

abstract class CustomChatbotMessageViewHolder(
        itemView: View?,
        protected val listener: ChatLinkHandlerListener,
//        private val adapterListener: AdapterListener
) : BaseChatViewHolder<MessageViewModel>(itemView) {

    protected open val customChatLayout: CustomChatbotChatLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.customChatLayout)
    protected open val msgContainer: ConstraintLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.cl_msg_container)
    protected val bottomMarginOpposite: Float = getOppositeMargin(itemView?.context)
    private val movementMethod = ChatLinkHandlerMovementMethod(listener)

    override fun bind(message: MessageViewModel) {
        verifyReplyTime(message)
        ChatbotMessageViewHolderBinder.bindChatMessage(message.message, customChatLayout, movementMethod, message.isSender)
        ChatbotMessageViewHolderBinder.bindHour(message.replyTime, customChatLayout)
        setHeaderDate(message)
    }

    protected fun verifyReplyTime(chat: MessageViewModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOppositeMargin(context: Context?): Float {
        return context?.resources?.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                ?: 0f
    }

    override fun getDateId(): Int = R.id.date

    companion object {
        val TYPE_LEFT = 0
        val TYPE_RIGHT = 1
    }
}