package com.tokopedia.play.ui.chatlist.adapter

import com.tokopedia.adapter_delegate.BaseAdapter
import com.tokopedia.play.ui.chatlist.adapter.delegate.ChatAdapterDelegate
import com.tokopedia.play.ui.chatlist.model.PlayChat

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapter : BaseAdapter<PlayChat>() {

    init {
        delegatesManager
                .addDelegate(ChatAdapterDelegate())
    }

    fun addChat(chat: PlayChat) {
        addItem(chat)
        notifyItemInserted(itemCount)
    }
}