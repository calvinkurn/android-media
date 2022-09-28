package com.tokopedia.tokochat_common.view.adapter.viewholder.message_bubble

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemMessageBubbleBinding
import com.tokopedia.tokochat_common.databinding.TokochatPartialMesssageBubbleBinding
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.getOppositeMargin
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.MILLISECONDS
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.START_YEAR
import com.tokopedia.tokochat_common.view.adapter.viewholder.common.TokoChatMessageBubbleViewHolderBinder
import com.tokopedia.tokochat_common.view.adapter.viewholder.common.TokoChatMessageBubbleViewHolderBinder.generateLeftBg
import com.tokopedia.tokochat_common.view.adapter.viewholder.common.TokoChatMessageBubbleViewHolderBinder.generateRightBg
import com.tokopedia.tokochat_common.view.uimodel.TokoChatMessageBubbleBaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatMessageBubbleViewHolder(itemView: View): BaseViewHolder(itemView) {

    private val binding: TokochatItemMessageBubbleBinding? by viewBinding()
    private val messageBubbleBinding: TokochatPartialMesssageBubbleBinding? by viewBinding()

    private val topMarginOpposite: Float = getOppositeMargin(itemView.context)
    private val bubbleToScreenMargin: Float = itemView.context?.resources?.getDimension(
        com.tokopedia.unifyprinciples.R.dimen.unify_space_12
    ) ?: 0f

    private val bgLeft = generateLeftBg(messageBubbleBinding?.messageChatLayout)
    private val bgRight = generateRightBg(messageBubbleBinding?.messageChatLayout)

    fun bind(msg: TokoChatMessageBubbleBaseUiModel) {
        verifyReplyTime(msg)
        TokoChatMessageBubbleViewHolderBinder.bindChatMessage(
            msg, messageBubbleBinding?.messageChatLayout)
        TokoChatMessageBubbleViewHolderBinder.bindHour(
            msg, messageBubbleBinding?.messageChatLayout)
        bindMargin(msg)
        bindClick()
        bindIcon(msg)
        bindTextColor(msg)
        bindMessageInfo(msg)
        if (msg.isSender) {
            setSenderMessageLayout(msg)
        } else {
            setReceiverLayout()
        }
    }

    /**
     * Right side message
     */
    private fun setSenderMessageLayout(msg: TokoChatMessageBubbleBaseUiModel) {
        bindMsgGravity(Gravity.END)
        paddingRightMsg()
        bindBackground(bgRight)
        TokoChatMessageBubbleViewHolderBinder.bindChatReadStatus(
            msg, messageBubbleBinding?.messageChatLayout)
    }

    /**
     * Left side message
     */
    private fun setReceiverLayout() {
        bindMsgGravity(Gravity.START)
        paddingLeftMsg()
        bindBackground(bgLeft)
        messageBubbleBinding?.messageChatLayout?.checkMark?.hide()
    }

    private fun bindTextColor(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.messageChatLayout?.bindTextColor(msg)
    }

    private fun bindIcon(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.messageChatLayout?.bindIcon(msg)
    }

    private fun bindMsgGravity(gravity: Int) {
        bindLayoutGravity(gravity)
        bindGravity(gravity)
        bindLayoutMsgGravity(gravity)
        binding?.bodyMessageBubbleLayout?.setMsgGravity(gravity)
    }

    private fun paddingRightMsg() {
        binding?.bubbleContainerLayout?.let {
            it.setPadding(
                0, it.paddingTop, bubbleToScreenMargin.toInt(), it.paddingBottom
            )
        }
    }

    private fun paddingLeftMsg() {
        binding?.bubbleContainerLayout?.let {
            it.setPadding(
                bubbleToScreenMargin.toInt(), it.paddingTop, 0, it.paddingBottom
            )
        }
    }

    private fun bindMessageInfo(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.messageChatLayout?.bindInfo(msg)
    }

    private fun verifyReplyTime(chat: TokoChatMessageBubbleBaseUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILLISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILLISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bindMargin(message: TokoChatMessageBubbleBaseUiModel) {
//        if (adapterListener.isOpposite(adapterPosition, message.isSender)) {
        if (message.isSender) {
            binding?.bubbleContainerLayout?.setMargin(0, topMarginOpposite.toInt(), 0, 0)
        } else {
            binding?.bubbleContainerLayout?.setMargin(0, 0, 0, 0)
        }
    }

    private fun bindClick() {
        itemView.setOnClickListener { v ->
            KeyboardHandler.DropKeyboard(
                itemView.context,
                itemView
            )
        }
    }

    private fun bindGravity(gravity: Int) {
        binding?.bubbleContainerLayout?.gravity = gravity
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.bubbleContainerLayout?.layoutParams as FrameLayout.LayoutParams
        containerLp.gravity = gravity
        binding?.bubbleContainerLayout?.layoutParams = containerLp
    }

    private fun bindLayoutMsgGravity(gravity: Int) {
        binding?.bodyMessageContainerLayout?.gravity = gravity
    }

    private fun bindBackground(drawable: Drawable?) {
        messageBubbleBinding?.messageChatLayout?.background = drawable
    }

    companion object {
        val LAYOUT = R.layout.tokochat_item_message_bubble
    }
}
