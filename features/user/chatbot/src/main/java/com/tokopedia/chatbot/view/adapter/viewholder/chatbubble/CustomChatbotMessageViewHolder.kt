package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.text.TextUtils
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero

abstract class CustomChatbotMessageViewHolder(
        itemView: View?,
        protected val listener: ChatLinkHandlerListener,
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    protected open val customChatLayout: CustomChatbotChatLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.customChatLayout)
    protected open val msgContainer: ConstraintLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.cl_msg_container)
    private val dateContainer: CardView? = itemView?.findViewById(getDateContainerId())

    open fun getDateContainerId(): Int = R.id.dateContainer

    private val movementMethod = ChatLinkHandlerMovementMethod(listener)

    override fun bind(message: MessageUiModel) {
        verifyReplyTime(message)
        ChatbotMessageViewHolderBinder.bindChatMessage(message.message, customChatLayout, movementMethod, message.isSender)
        ChatbotMessageViewHolderBinder.bindHour(message.replyTime, customChatLayout)
        setHeaderDate(message)
    }

    protected fun verifyReplyTime(chat: MessageUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setHeaderDate(element: BaseChatUiModel?) {
        if (date == null) return
        val time = element?.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                    it,
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date))
        }
        date.text = time
        if (date != null && element?.isShowDate ==true
                && !TextUtils.isEmpty(time)) {
            dateContainer?.show()
        } else if (date != null) {
            dateContainer?.hide()
        }
    }

    override fun getDateId(): Int = R.id.date

    companion object {
        val TYPE_LEFT = 0
        val TYPE_RIGHT = 1
    }
}