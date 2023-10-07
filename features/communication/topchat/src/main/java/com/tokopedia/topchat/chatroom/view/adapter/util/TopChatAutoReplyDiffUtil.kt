package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyUiModel

class TopChatAutoReplyDiffUtil: DiffUtil.ItemCallback<TopChatAutoReplyUiModel>() {

    override fun areItemsTheSame(
        oldItem: TopChatAutoReplyUiModel,
        newItem: TopChatAutoReplyUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TopChatAutoReplyUiModel,
        newItem: TopChatAutoReplyUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
