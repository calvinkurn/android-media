package com.tokopedia.chatbot.chatbot2.view.util.helper

import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.view.customview.CustomChatbotChatLayout
import com.tokopedia.chatbot.view.customview.MessageBubbleLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

object ChatbotMessageViewHolderBinder {

    fun bindChatMessage(
        chat: String,
        chatlayout: CustomChatbotChatLayout?,
        movementMethod: ChatLinkHandlerMovementMethod,
        isSender: Boolean = false
    ) {
        chatlayout?.setMessage(chat, isSender)
        chatlayout?.setMovementMethod(movementMethod)
    }

    fun bindChatMessage(
        chat: String,
        messageBubble: MessageBubbleLayout?,
        movementMethod: ChatLinkHandlerMovementMethod,
        isSender: Boolean = false
    ) {
        messageBubble?.fxChat?.setMessage(chat, isSender)
        messageBubble?.fxChat?.setMovementMethod(movementMethod)
    }

    fun bindHour(
        replyTime: String?,
        chatLayout: CustomChatbotChatLayout?
    ) {
        val hourTime = getHourTime(replyTime)
        chatLayout?.setHourTime(hourTime)
    }

    fun getTime(
        replyTime: String?
    ): String {
        return getHourTime(replyTime)
    }

    fun bindHour(
        replyTime: String?,
        messageBubble: MessageBubbleLayout?
    ) {
        val hourTime = getHourTime(replyTime)
        messageBubble?.fxChat?.setHourTime(hourTime)
    }

    fun bindHourTextView(
        uiModel: DynamicOwocInvoiceUiModel,
        hour: TextView?
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        hour?.text = hourTime
    }

    private fun getHourTime(replyTime: String?): String {
        return try {
            replyTime?.let {
                ChatTimeConverter.formatTime(replyTime.toLong() / BaseChatViewHolder.MILISECONDS)
            } ?: ""
        } catch (e: NumberFormatException) {
            replyTime ?: ""
        }
    }

    fun bindChatReadStatus(element: MessageUiModel, messageView: CustomChatbotChatLayout?) {
        messageView?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }

    fun bindChatReadStatus(element: MessageUiModel, messageBubble: MessageBubbleLayout?) {
        messageBubble?.fxChat?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }

    fun bindChatReadStatus(element: SendableUiModel, checkMark: ImageView) {
        if (element.isShowTime && element.isSender) {
            checkMark.show()
            val imageResource = when {
                element.isDummy -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_rounded_grey
                else -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }

    fun bindChatReadStatus(element: SendableUiModel, messageBubble: MessageBubbleLayout?) {
        messageBubble?.fxChat?.checkMark?.let {
            bindChatReadStatus(element, it)
        }
    }
}
