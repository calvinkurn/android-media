package com.tokopedia.play.ui.quickreply.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.chatlist.viewholder.ChatViewHolder

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyAdapterDelegate(
        private val onQuickReplyClicked: (String) -> Unit
) : TypedAdapterDelegate<String, String, ChatViewHolder>(R.layout.item_play_quick_reply) {

    override fun onBindViewHolder(item: String, holder: ChatViewHolder) {
        holder.bind(item, onQuickReplyClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChatViewHolder {
        return ChatViewHolder(basicView)
    }
}