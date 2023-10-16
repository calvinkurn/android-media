package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply.TopChatChatRoomAutoReplyItemViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatChatRoomAutoReplyDiffUtil

class TopChatChatRoomAutoReplyAdapter (
    private val isMessageBubble: Boolean = true
): ListAdapter<TopChatAutoReplyItemUiModel, TopChatChatRoomAutoReplyItemViewHolder>(
    TopChatChatRoomAutoReplyDiffUtil()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopChatChatRoomAutoReplyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.topchat_chatroom_auto_reply_item, parent, false
        )
        return TopChatChatRoomAutoReplyItemViewHolder(view, isMessageBubble)
    }

    override fun getItemCount(): Int = this.currentList.size

    override fun onBindViewHolder(holder: TopChatChatRoomAutoReplyItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateItem(list: List<TopChatAutoReplyItemUiModel>) {
        this.submitList(list)
    }
}
