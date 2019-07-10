package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachImageViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.ProductLinkViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory
import com.tokopedia.topchat.R

class TopChatChatMenuFactory : ChatMenuFactory {
    override fun create(listener: BaseChatMenuViewHolder.ChatMenuListener, view: View, position: Int): BaseChatMenuViewHolder {
        val errorMessage = "Unknown ChatMenuViewHolder on: $position"
        return when (position) {
            0 -> ProductLinkViewHolder(listener, view)
            1 -> AttachImageViewHolder(listener, view)
            else -> throw IllegalStateException(errorMessage)
        }
    }

    override fun createChatMenuItems(): List<ChatMenu> {
        return listOf(
                ChatMenu(icon = R.drawable.ic_attach_grey, title = "Link Produk"),
                ChatMenu(icon = R.drawable.ic_image_picker_grey, title = "Gambar")
        )
    }

}