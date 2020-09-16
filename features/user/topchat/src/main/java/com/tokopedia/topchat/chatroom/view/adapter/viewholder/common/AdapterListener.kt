package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common

interface AdapterListener {
    fun isNextItemSender(adapterPosition: Int, isSender: Boolean): Boolean
}