package com.tokopedia.topchat.chatroom.view.adapter.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatRoomAutoReplyItemUiModel

class TopChatChatRoomAutoReplyDiffUtil: DiffUtil.ItemCallback<TopChatRoomAutoReplyItemUiModel>() {

    override fun areItemsTheSame(
        oldItem: TopChatRoomAutoReplyItemUiModel,
        newItem: TopChatRoomAutoReplyItemUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: TopChatRoomAutoReplyItemUiModel,
        newItem: TopChatRoomAutoReplyItemUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
