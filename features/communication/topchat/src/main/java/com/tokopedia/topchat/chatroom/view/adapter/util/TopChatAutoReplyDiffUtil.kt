package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel

class TopChatAutoReplyDiffUtil: DiffUtil.ItemCallback<TopChatAutoReplyItemUiModel>() {

    override fun areItemsTheSame(
        oldItem: TopChatAutoReplyItemUiModel,
        newItem: TopChatAutoReplyItemUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TopChatAutoReplyItemUiModel,
        newItem: TopChatAutoReplyItemUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
