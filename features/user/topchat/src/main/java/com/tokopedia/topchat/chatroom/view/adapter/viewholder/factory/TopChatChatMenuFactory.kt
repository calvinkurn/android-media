package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachImageViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.ProductLinkViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory

class TopChatChatMenuFactory : ChatMenuFactory {
    override fun create(listener: BaseChatMenuViewHolder.ChatMenuListener, view: View, position: Int): BaseChatMenuViewHolder {
        return when (position) {
            0 -> ProductLinkViewHolder(listener, view)
            1 -> AttachImageViewHolder(listener, view)
            else -> throw IllegalStateException("Unknown ViewHolder on: $position")
        }
    }

    override fun createChatMenuItems(): List<ChatMenu> {
        return listOf(
                ChatMenu(icon = com.tokopedia.chat_common.R.drawable.ic_attach_grey, title = "Link Produk", label = "product"),
                ChatMenu(icon = com.tokopedia.chat_common.R.drawable.ic_image_picker_grey, title = "Gambar", label = "image")
        )
    }
}