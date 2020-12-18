package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.common.util.ViewUtil

class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : ChatMessageViewHolder(itemView, listener, adapterListener) {

    private val headerInfo: LinearLayout? = itemView?.findViewById(R.id.ll_header_info)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            fxChat,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindMessageInfo(message)
        bindBackground(message)
        bindHeaderInfo(message)
    }

    private fun bindHeaderInfo(message: MessageViewModel) {
        if (
                message.source == BaseChatViewModel.SOURCE_REPLIED_BLAST &&
                !message.isSender &&
                commonListener.isSeller()
        ) {
            headerInfo?.show()
        } else {
            headerInfo?.hide()
        }
    }

    private fun bindBackground(message: MessageViewModel) {
        fxChat?.background = bg
    }

    private fun bindMessageInfo(message: MessageViewModel) {
        if (
                message.source == BaseChatViewModel.SOURCE_AUTO_REPLY &&
                !message.isSender &&
                !commonListener.isSeller()
        ) {
            fxChat?.showInfo()
        } else {
            fxChat?.hideInfo()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_left
    }
}