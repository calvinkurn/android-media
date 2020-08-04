package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.ui.chat.delegate.PlayChatAdapterDelegate
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 09/06/20
 */
class ChatListAdapter : BaseAdapter<PlayChatUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayChatAdapterDelegate(typographyType = Typography.BODY_2))
    }

    fun addChat(chat: PlayChatUiModel) {
        addItem(chat)
        notifyItemInserted(itemCount)
    }

    fun setChatList(chatList: List<PlayChatUiModel>) {
        setItems(chatList)
        notifyDataSetChanged()
    }
}