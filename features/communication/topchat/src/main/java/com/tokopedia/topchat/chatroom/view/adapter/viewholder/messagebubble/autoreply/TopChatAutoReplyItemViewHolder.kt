package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.autoreply

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel
import com.tokopedia.topchat.databinding.TopchatChatroomAutoReplyItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class TopChatAutoReplyItemViewHolder(
    itemView: View,
    private val shouldLimitText: Boolean
): RecyclerView.ViewHolder(itemView) {

    private val binding: TopchatChatroomAutoReplyItemBinding? by viewBinding()

    fun bind(uiModel: TopChatAutoReplyItemUiModel) {
        bindIcon(uiModel)
        bindTitle(uiModel)
        bindMessage(uiModel)
    }

    private fun bindIcon(uiModel: TopChatAutoReplyItemUiModel) {
        val icon = uiModel.getIcon()
        if (icon != null) {
            binding?.topchatIconAutoReply?.setImage(icon)
            binding?.topchatIconAutoReply?.show()
        } else {
            binding?.topchatIconAutoReply?.hide()
        }
    }

    private fun bindTitle(uiModel: TopChatAutoReplyItemUiModel) {
        binding?.topchatTvAutoReplyTitle?.text = uiModel.title
    }

    private fun bindMessage(uiModel: TopChatAutoReplyItemUiModel) {
        if (shouldLimitText) {
            binding?.topchatTvAutoReplyDesc?.maxLines = 3
            binding?.topchatTvAutoReplyDesc?.ellipsize = TextUtils.TruncateAt.END
        }
        binding?.topchatTvAutoReplyDesc?.text = uiModel.getMessage()
    }
}
