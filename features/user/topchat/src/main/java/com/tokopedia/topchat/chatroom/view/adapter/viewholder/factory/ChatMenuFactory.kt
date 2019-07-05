package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.ChatMenu
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatmenu.ProductLinkViewHolder

object ChatMenuFactory {

    fun create(listener: BaseChatMenuViewHolder.ChatMenuListener, view: View, position: Int): BaseChatMenuViewHolder {
        return when (position) {
            0 -> ProductLinkViewHolder(listener, view)
            1 -> ProductLinkViewHolder(listener, view)
            2 -> ProductLinkViewHolder(listener, view)
            3 -> ProductLinkViewHolder(listener, view)
            else -> throw IllegalStateException("Unknown ViewHolder on: $position")
        }
    }

    fun createChatMenuItems(): List<ChatMenu> {
        return listOf(
                ChatMenu(R.drawable.ic_attach_grey, "Link Produk"),
                ChatMenu(R.drawable.ic_attach_grey, "Gambar"),
                ChatMenu(R.drawable.ic_attach_grey, "Daftar Transaksi"),
                ChatMenu(R.drawable.ic_attach_grey, "Voucher Toko")
        )
    }

}