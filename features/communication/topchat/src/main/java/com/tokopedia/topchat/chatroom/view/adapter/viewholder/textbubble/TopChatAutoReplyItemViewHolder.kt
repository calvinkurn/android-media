package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomAutoReplyItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatAutoReplyItemViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val binding: TopchatChatroomAutoReplyItemBinding? by viewBinding()

    fun bind(uiModel: TopChatAutoReplyUiModel) {
        bindIcon(uiModel)
        bindTitle(uiModel)
        bindMessage(uiModel)
    }

    private fun bindIcon(uiModel: TopChatAutoReplyUiModel) {
        val icon = uiModel.getIcon()
        if (icon != null) {
            binding?.topchatIconAutoReply?.setImage(icon)
            binding?.topchatIconAutoReply?.show()
        } else {
            binding?.topchatIconAutoReply?.hide()
        }
    }

    private fun bindTitle(uiModel: TopChatAutoReplyUiModel) {
        binding?.topchatTvAutoReplyTitle?.text = uiModel.title
    }

    private fun bindMessage(uiModel: TopChatAutoReplyUiModel) {
        binding?.topchatTvAutoReplyDesc?.text = uiModel.getMessage()
    }
}
