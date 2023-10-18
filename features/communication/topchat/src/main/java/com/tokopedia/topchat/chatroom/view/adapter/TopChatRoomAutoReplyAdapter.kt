package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply.TopChatRoomAutoReplyItemViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatRoomAutoReplyItemUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatChatRoomAutoReplyDiffUtil

class TopChatRoomAutoReplyAdapter (
    private val isMessageBubble: Boolean = true
): ListAdapter<TopChatRoomAutoReplyItemUiModel, TopChatRoomAutoReplyItemViewHolder>(
    TopChatChatRoomAutoReplyDiffUtil()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopChatRoomAutoReplyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.topchat_chatroom_auto_reply_item, parent, false
        )
        return TopChatRoomAutoReplyItemViewHolder(view, isMessageBubble)
    }

    override fun getItemCount(): Int = this.currentList.size

    override fun onBindViewHolder(holder: TopChatRoomAutoReplyItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateItem(list: List<TopChatRoomAutoReplyItemUiModel>) {
        this.submitList(list)
    }
}
