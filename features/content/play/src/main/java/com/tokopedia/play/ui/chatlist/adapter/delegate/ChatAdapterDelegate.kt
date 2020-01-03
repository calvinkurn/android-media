package com.tokopedia.play.ui.chatlist.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.chatlist.viewholder.ChatViewHolder

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapterDelegate : TypedAdapterDelegate<PlayChat, PlayChat, ChatViewHolder>(R.layout.item_play_chat) {

    override fun onBindViewHolder(item: PlayChat, holder: ChatViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChatViewHolder {
        return ChatViewHolder(basicView)
    }
}