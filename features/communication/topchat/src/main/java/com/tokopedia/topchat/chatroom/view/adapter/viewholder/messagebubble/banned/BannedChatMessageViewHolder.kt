package com.tokopedia.topchat.chatroom.view.adapter.viewholder.messagebubble.banned

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.util.ChatTimeConverter.getHourTime
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomBubbleBackgroundGenerator.generateLeftBg
import com.tokopedia.topchat.chatroom.view.adapter.util.TopChatRoomBubbleBackgroundGenerator.generateRightBg
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin
import com.tokopedia.topchat.databinding.TopchatChatroomChatBubbleBannedItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class BannedChatMessageViewHolder(
    itemView: View,
    private val listener: TopChatMessageCensorListener,
    private val adapterListener: AdapterListener
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    private val binding: TopchatChatroomChatBubbleBannedItemBinding? by viewBinding()

    private val bgLeft = generateLeftBg(binding?.clBanContainer)
    private val bgRight = generateRightBg(binding?.clBanContainer)

    override fun bind(uiModel: MessageUiModel) {
        verifyReplyTime(uiModel)
        bindMargin(uiModel)
        bindCensorText()
        bindClickInfo()
        bindBackground(uiModel)
        bindGravity(uiModel)
        bindHourTextView(uiModel, binding?.tvTime)
    }

    private fun bindHourTextView(
        uiModel: MessageUiModel,
        hour: TextView?
    ) {
        val hourTime = getHourTime(uiModel.replyTime)
        hour?.text = hourTime
    }

    private fun bindGravity(message: MessageUiModel) {
        if (message.isSender) {
            binding?.clMsgContainer?.gravity = Gravity.END
        } else {
            binding?.clMsgContainer?.gravity = Gravity.START
        }
    }

    private fun bindClickInfo() {
        binding?.txtInfo?.setOnClickListener {
            listener.onClickCheckGuide()
        }
    }

    private fun bindBackground(message: MessageUiModel) {
        if (message.isSender) {
            binding?.clBanContainer?.background = bgRight
        } else {
            binding?.clBanContainer?.background = bgLeft
        }
    }

    private fun bindMargin(message: MessageUiModel) {
        val lp = binding?.clMsgContainer?.layoutParams
        if (lp is RecyclerView.LayoutParams) {
            if (adapterListener.isOpposite(adapterPosition, message.isSender)) {
                binding?.clMsgContainer?.setMargin(
                    0,
                    getOppositeMargin(itemView.context).toInt(),
                    0,
                    0
                )
            } else {
                binding?.clMsgContainer?.setMargin(0, 0, 0, 0)
            }
        }
    }

    private fun verifyReplyTime(chat: MessageUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindCensorText() {
        binding?.tvMessage?.setText(R.string.topchat_message_censored)
    }

    interface TopChatMessageCensorListener {
        fun onClickCheckGuide()
    }

    companion object {
        val LAYOUT = R.layout.topchat_chatroom_chat_bubble_banned_item
    }
}
