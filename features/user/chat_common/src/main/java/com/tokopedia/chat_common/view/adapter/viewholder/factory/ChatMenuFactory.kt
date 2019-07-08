package com.tokopedia.chat_common.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachImageViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.ProductLinkViewHolder

object ChatMenuFactory {

    fun create(listener: BaseChatMenuViewHolder.ChatMenuListener, view: View, position: Int): BaseChatMenuViewHolder {
        return when (position) {
            0 -> ProductLinkViewHolder(listener, view)
            1 -> AttachImageViewHolder(listener, view)
            else -> throw IllegalStateException("Unknown ViewHolder on: $position")
        }
    }

    fun createChatMenuItems(): List<ChatMenu> {
        return listOf(
                ChatMenu(R.drawable.ic_attach_grey, "Link Produk"),
                ChatMenu(R.drawable.ic_image_picker_grey, "Gambar")
        )
    }

}