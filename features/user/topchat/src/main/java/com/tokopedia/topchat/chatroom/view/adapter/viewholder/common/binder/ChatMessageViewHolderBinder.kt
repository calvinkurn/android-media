package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.util.ChatTimeConverter
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout

object ChatMessageViewHolderBinder {

    fun bindChatMessage(
            chat: MessageViewModel,
            fxChat: FlexBoxChatLayout?,
            movementMethod: ChatLinkHandlerMovementMethod
    ) {
        val htmlMessage = MethodChecker.fromHtml(chat.message)
        fxChat?.setMessage(htmlMessage)
        fxChat?.setMovementMethod(movementMethod)
    }

    fun bindHour(
            viewModel: MessageViewModel,
            fxChat: FlexBoxChatLayout?
    ) {
        val hourTime = getHourTime(viewModel.replyTime)
        fxChat?.setHourTime(hourTime)
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

    fun bindChatReadStatus(element: MessageViewModel, messageView: FlexBoxChatLayout?) {
        if (element.isShowTime && element.isSender) {
            messageView?.checkMark?.show()
            val imageResource = when {
                element.isDummy -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_rounded_grey
                !element.isRead -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_sent_rounded_grey
                else -> com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(messageView?.checkMark?.context, imageResource)
            messageView?.checkMark?.setImageDrawable(drawable)
        } else {
            messageView?.checkMark?.gone()
        }
    }

}