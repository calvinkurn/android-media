package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.chatbubble

import android.text.TextUtils
import android.view.View
import androidx.cardview.widget.CardView
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatBotTimeConverter
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero

abstract class CustomChatbotMessageViewHolder(
    itemView: View?,
    protected val listener: ChatLinkHandlerListener,
    val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    protected open val customChatLayout: CustomChatbotChatLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.customChatLayout)
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

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                it,
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
            )
        }
        date?.text = time
        if (date != null && element.isShowDate && !TextUtils.isEmpty(time)) {
            dateContainer?.show()
        } else if (date != null) {
            dateContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date

    companion object {
        const val TYPE_LEFT = 0
        const val TYPE_RIGHT = 1
    }
}
