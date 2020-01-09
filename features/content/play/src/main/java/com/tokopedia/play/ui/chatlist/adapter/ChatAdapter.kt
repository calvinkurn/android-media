package com.tokopedia.play.ui.chatlist.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play.ui.chatlist.adapter.delegate.ChatAdapterDelegate
import com.tokopedia.play.view.uimodel.PlayChatUiModel

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapter : BaseAdapter<PlayChatUiModel>() {

    init {
        delegatesManager
                .addDelegate(ChatAdapterDelegate())
    }

    fun addChat(chat: PlayChatUiModel) {
        addItem(chat)
        notifyItemInserted(itemCount)
    }
}