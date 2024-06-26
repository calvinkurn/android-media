package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatRoomAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomAutoReplyItemBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.Typography

class TopChatRoomAutoReplyItemViewHolder(
    itemView: View,
    private val isMessageBubble: Boolean
): RecyclerView.ViewHolder(itemView) {

    private val binding: TopchatChatroomAutoReplyItemBinding? by viewBinding()

    fun bind(uiModel: TopChatRoomAutoReplyItemUiModel) {
        bindIcon(uiModel)
        bindTitle(uiModel)
        bindMessage(uiModel)
    }

    private fun bindIcon(uiModel: TopChatRoomAutoReplyItemUiModel) {
        val icon = uiModel.getIcon()
        if (icon != null) {
            binding?.topchatIconAutoReply?.setImage(icon)
            binding?.topchatIconAutoReply?.show()
        } else {
            binding?.topchatIconAutoReply?.hide()
        }
    }

    private fun bindTitle(uiModel: TopChatRoomAutoReplyItemUiModel) {
        binding?.topchatTvAutoReplyTitle?.text = uiModel.title
    }

    private fun bindMessage(uiModel: TopChatRoomAutoReplyItemUiModel) {
        if (isMessageBubble) { // Limit the text when in message bubble
            binding?.topchatTvAutoReplyDesc?.maxLines = 3
            binding?.topchatTvAutoReplyDesc?.ellipsize = TextUtils.TruncateAt.END
            binding?.topchatTvAutoReplyDesc?.setType(Typography.DISPLAY_1)
        }
        binding?.topchatTvAutoReplyDesc?.text = uiModel.getMessage()
    }
}
