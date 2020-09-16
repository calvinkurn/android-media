package com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout

abstract class FallbackMessageViewHolder(itemView: View?, private val listener: ChatLinkHandlerListener)
    : BaseChatViewHolder<FallbackAttachmentViewModel>(itemView) {

    open val fxChat: FlexBoxChatLayout? = itemView?.findViewById(R.id.fxChat)

    override fun bind(viewModel: FallbackAttachmentViewModel?) {
        if (viewModel == null) return
        verifyReplyTime(viewModel)
        bindChatMessage(viewModel)
        bindHour(viewModel)
        bindHeaderDate(viewModel)
        bindClick()
    }

    override fun getDateId(): Int {
        return R.id.tvDate
    }

    protected fun bindClick() {
        itemView.setOnClickListener { v -> KeyboardHandler.DropKeyboard(itemView.context, itemView) }
    }

    protected fun bindChatMessage(chat: FallbackAttachmentViewModel) {
        val movementMethod = ChatLinkHandlerMovementMethod(listener)
        val htmlMessage = MethodChecker.fromHtml(chat.message)
        fxChat?.setMessage(htmlMessage)
        fxChat?.setMovementMethod(movementMethod)
    }

    protected fun bindHour(viewModel: FallbackAttachmentViewModel) {
        val hourTime = getHourTime(viewModel.replyTime)
        fxChat?.setHourTime(hourTime)
    }

    protected fun bindHeaderDate(chat: FallbackAttachmentViewModel) {
        setHeaderDate(chat)
    }

    protected fun verifyReplyTime(chat: FallbackAttachmentViewModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TYPE_LEFT = 2
        const val TYPE_RIGHT = 3
    }
}