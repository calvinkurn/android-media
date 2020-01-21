package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.ROLE_USER
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import kotlinx.android.synthetic.main.item_topchat_chat_left.view.*
import kotlinx.android.synthetic.main.item_topchat_header_role_user.view.*

abstract class ChatMessageViewHolder(itemView: View?) : BaseChatViewHolder<MessageViewModel>(itemView) {

    override fun bind(viewModel: MessageViewModel?) {
        if (viewModel == null) return
        verifyReplyTime(viewModel)
        bindChatMessage(viewModel)
        bindHour(viewModel)
        bindHeaderDate(viewModel)
        bindHeaderRole(viewModel)
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
            itemView.tvRole?.text = chat.from
            itemView.tvName?.text = chat.fromRole
            itemView.llRoleUser?.show()
        } else {
            itemView.llRoleUser?.hide()
        }
    }

    private fun bindHeaderDate(viewModel: MessageViewModel) {
        setHeaderDate(viewModel)
    }

    override fun getDateId(): Int {
        return R.id.tvDate
    }

    protected fun bindChatMessage(chat: MessageViewModel) {
        itemView.fxChat?.setMessage(chat.message)
    }

    protected fun bindHour(viewModel: MessageViewModel) {
        val hourTime = getHourTime(viewModel.replyTime)
        itemView.fxChat?.setHourTime(hourTime)
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