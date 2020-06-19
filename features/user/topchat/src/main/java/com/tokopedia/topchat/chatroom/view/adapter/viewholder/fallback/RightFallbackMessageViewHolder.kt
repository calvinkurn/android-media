package com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback

import android.view.View
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R

class RightFallbackMessageViewHolder(itemView: View?, listener: ChatLinkHandlerListener)
    : FallbackMessageViewHolder(itemView, listener) {

    override fun alwaysShowTime(): Boolean = true

    override fun bind(viewModel: FallbackAttachmentViewModel?) {
        if (viewModel == null) return
        super.bind(viewModel)
        bindCheckMark(viewModel)
    }

    private fun bindCheckMark(viewModel: FallbackAttachmentViewModel) {
        chatReadStatus.setImageResource(com.tokopedia.chat_common.R.drawable.ic_chatcommon_check_read_rounded_green)
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right_fallback
    }
}