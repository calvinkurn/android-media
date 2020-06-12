package com.tokopedia.play.ui.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.ui.chatlist.viewholder.ChatViewHolder
import com.tokopedia.play.view.uimodel.PlayChatUiModel

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapterDelegate : TypedAdapterDelegate<PlayChatUiModel, PlayChatUiModel, ChatViewHolder>(com.tokopedia.play_common.R.layout.item_play_chat) {

    override fun onBindViewHolder(item: PlayChatUiModel, holder: ChatViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChatViewHolder {
        return ChatViewHolder(basicView)
    }
}