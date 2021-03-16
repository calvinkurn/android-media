package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.text.SimpleDateFormat
import java.util.*

abstract class CustomChatbotMessageViewHolder(
        itemView: View?,
        protected val listener: ChatLinkHandlerListener,
) : BaseChatViewHolder<MessageViewModel>(itemView) {

    protected open val customChatLayout: CustomChatbotChatLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.customChatLayout)
    protected open val msgContainer: ConstraintLayout? = itemView?.findViewById(com.tokopedia.chatbot.R.id.cl_msg_container)
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

    override fun setHeaderDate(element: BaseChatViewModel?) {
        if (date == null) return
        var time: String? = ""

        try {
            var myTime = element?.replyTime?.toLong()
            if (myTime!=null){
                myTime = myTime / MILISECONDS
                val date = Date(myTime)
                time = if (DateUtils.isToday(myTime)) {
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_today_date)
                } else if (DateUtils.isToday(myTime + DateUtils.DAY_IN_MILLIS)) {
                    itemView.context.getString(com.tokopedia.chat_common.R.string.chat_yesterday_date)
                } else {
                    val formatter = SimpleDateFormat("d MMM")
                    formatter.format(date)
                }
            }

        } catch (e: NumberFormatException) {
            time = element?.replyTime
        }

        if (date != null && element?.isShowDate ==true
                && !TextUtils.isEmpty(time)) {
            date.show()
            date.text = time
        } else if (date != null) {
            date.hide()
        }
    }

    override fun getDateId(): Int = R.id.date

    companion object {
        val TYPE_LEFT = 0
        val TYPE_RIGHT = 1
    }
}