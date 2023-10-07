package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble.TopChatAutoReplyItemViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyUiModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatAutoReplyDiffUtil

class TopChatAutoReplyAdapter
    : ListAdapter<TopChatAutoReplyUiModel, TopChatAutoReplyItemViewHolder>(
    TopChatAutoReplyDiffUtil()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopChatAutoReplyItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.topchat_chatroom_auto_reply_item, parent, false
        )
        return TopChatAutoReplyItemViewHolder(view)
    }

    override fun getItemCount(): Int = this.currentList.size

    override fun onBindViewHolder(holder: TopChatAutoReplyItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun updateItem(list: List<TopChatAutoReplyUiModel>) {
        this.submitList(list)
    }
}
