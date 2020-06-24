package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.unifyprinciples.Typography

class RightChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val commonListener: CommonViewHolderListener
) : ChatMessageViewHolder(itemView, listener) {

    var header: LinearLayout? = itemView?.findViewById(R.id.llRoleUser)
    var headerRole: Typography? = itemView?.findViewById(R.id.tvRole)

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindChatReadStatus(message)
        bindHeader(message)
    }

    private fun bindHeader(message: MessageViewModel) {
        if (
                message.source == BaseChatViewModel.SOURCE_AUTO_REPLY &&
                message.source == BaseChatViewModel.SOURCE_TOPBOT &&
                message.isSender &&
                commonListener.isSeller()
        ) {
            val headerRoleText = itemView.context?.getString(R.string.tittle_header_auto_reply)
            header?.show()
            headerRole?.show()
            headerRole?.text = headerRoleText
        } else {
            header?.hide()
        }
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right
    }
}