package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.ROLE_USER
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.custom.FlexBoxChatLayout

abstract class ChatMessageViewHolder(itemView: View?, private val listener: ChatLinkHandlerListener)
    : BaseChatViewHolder<MessageViewModel>(itemView) {

    open val tvRole: TextView? = itemView?.findViewById(R.id.tvRole)
    open val tvName: TextView? = itemView?.findViewById(R.id.tvName)
    open val llRoleUser: LinearLayout? = itemView?.findViewById(R.id.llRoleUser)
    open val fxChat: FlexBoxChatLayout? = itemView?.findViewById(R.id.fxChat)

    override fun bind(viewModel: MessageViewModel?) {
        if (viewModel == null) return
        verifyReplyTime(viewModel)
        bindChatMessage(viewModel)
        bindHour(viewModel)
        bindHeaderDate(viewModel)
        bindClick()
    }

    protected fun bindClick() {
        itemView.setOnClickListener { v -> KeyboardHandler.DropKeyboard(itemView.context, itemView) }
    }

    override fun getDateId(): Int {
        return R.id.tvDate
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

    @SuppressLint("DefaultLocale")
    private fun bindHeaderRole(chat: MessageViewModel) {
        if (
                chat.fromRole.isNotEmpty() &&
                chat.fromRole.toLowerCase() != ROLE_USER.toLowerCase() &&
                chat.isSender &&
                !chat.isDummy &&
                chat.isShowRole
        ) {
            tvRole?.text = chat.from
            tvName?.text = chat.fromRole
            llRoleUser?.show()
        } else {
            llRoleUser?.hide()
        }
    }

    private fun bindHeaderDate(viewModel: MessageViewModel) {
        setHeaderDate(viewModel)
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