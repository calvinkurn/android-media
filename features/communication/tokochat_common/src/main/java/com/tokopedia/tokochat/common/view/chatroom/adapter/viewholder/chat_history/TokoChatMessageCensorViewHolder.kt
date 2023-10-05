package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.tokochat_common.databinding.TokochatItemMessageCensorBinding
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageCensorListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatMessageBubbleCensorUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatMessageCensorViewHolder(
    itemView: View,
    private val listener: TokoChatMessageCensorListener
): BaseViewHolder(itemView) {
    private val binding: TokochatItemMessageCensorBinding? by viewBinding()

    fun bind(msg: TokoChatMessageBubbleCensorUiModel) {
        bindCensorText()
        bindTermsCondition()
        bindTime(msg)
        bindBackground()
    }

    private fun bindCensorText() {
        binding?.tokochatTvMsgCensor?.setText(R.string.tokochat_message_censored)
    }

    private fun bindTermsCondition() {
        binding?.tokochatTvMsgCensorGuide?.setOnClickListener {
            listener.onClickCheckGuide()
        }
    }

    private fun bindTime(msg: TokoChatMessageBubbleCensorUiModel) {
        binding?.tokochatTvMsgCensorTime?.text = TokoChatMessageBubbleViewHolderBinder
            .getHourTime(msg.messageTime)
    }

    private fun bindBackground() {
        val bgRight = TokoChatMessageBubbleViewHolderBinder
            .generateRightBg(binding?.tokochatLayoutBodyMsgCensorContainer)
        binding?.tokochatLayoutBodyMsgCensorContainer?.background = bgRight
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_message_censor
    }
}
