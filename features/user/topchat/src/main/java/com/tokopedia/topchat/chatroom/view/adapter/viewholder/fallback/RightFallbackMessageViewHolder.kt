package com.tokopedia.topchat.chatroom.view.adapter.viewholder.fallback

import android.view.View
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R

class RightFallbackMessageViewHolder(itemView: View?, listener: ChatLinkHandlerListener)
    : FallbackMessageViewHolder(itemView, listener) {

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right_fallback
    }
}