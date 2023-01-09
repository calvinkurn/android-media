package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.text.TextUtils
import android.view.View
import androidx.cardview.widget.CardView
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.MessageBubbleLayout
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSessionInterface

abstract class ChatbotMessageUnifyViewHolder(
    itemView: View?,
    protected val listener: ChatLinkHandlerListener,
    private val replyBubbleListener : ReplyBubbleAreaMessage.Listener,
    private val userSession: UserSessionInterface
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    protected open val customChatLayout: MessageBubbleLayout? = itemView?.findViewById(R.id.message_layout_with_reply)
    private val dateContainer: CardView? = itemView?.findViewById(getDateContainerId())

    open fun getDateContainerId(): Int = R.id.dateContainer

    private val movementMethod = ChatLinkHandlerMovementMethod(listener)

    override fun bind(message: MessageUiModel) {
        verifyReplyTime(message)
        ChatbotMessageViewHolderBinder.bindChatMessage(message.message, customChatLayout, movementMethod, message.isSender)
        ChatbotMessageViewHolderBinder.bindHour(message.replyTime, customChatLayout)
        setHeaderDate(message)
        bindReplyBubbleListener()

        customChatLayout?.fxChat?.setOnLongClickListener {
            replyBubbleListener.showReplyOption(message)
            return@setOnLongClickListener true
        }
        customChatLayout?.fxChat?.message?.setOnLongClickListener {
            replyBubbleListener.showReplyOption(message)
            return@setOnLongClickListener true
        }
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

    private fun bindReplyBubbleListener() {
        customChatLayout?.setReplyListener(replyBubbleListener)
    }

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element?.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                    it,
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date),
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date))
        }
        date?.text = time
        if (date != null && element.isShowDate ==true
                && !TextUtils.isEmpty(time)) {
            dateContainer?.show()
        } else if (date != null) {
            dateContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date

}
