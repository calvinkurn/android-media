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
import com.tokopedia.tokochat_common.databinding.TokochatPartialMessageBubbleBinding
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
    private val messageBubbleBinding: TokochatPartialMessageBubbleBinding? by viewBinding()

    private val topMarginOpposite: Float = getOppositeMargin(itemView.context)
    private val bubbleToScreenMargin: Float = itemView.context?.resources?.getDimension(
        com.tokopedia.unifyprinciples.R.dimen.unify_space_12
    ) ?: 0f

    private val bgLeft = generateLeftBg(messageBubbleBinding?.tokochatLayoutMessageChat)
    private val bgRight = generateRightBg(messageBubbleBinding?.tokochatLayoutMessageChat)

    fun bind(msg: TokoChatMessageBubbleBaseUiModel) {
        verifyReplyTime(msg)
        TokoChatMessageBubbleViewHolderBinder.bindChatMessage(
            msg, messageBubbleBinding?.tokochatLayoutMessageChat)
        TokoChatMessageBubbleViewHolderBinder.bindHour(
            msg, messageBubbleBinding?.tokochatLayoutMessageChat)
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
            msg, messageBubbleBinding?.tokochatLayoutMessageChat)
    }

    /**
     * Left side message
     */
    private fun setReceiverLayout() {
        bindMsgGravity(Gravity.START)
        paddingLeftMsg()
        bindBackground(bgLeft)
        messageBubbleBinding?.tokochatLayoutMessageChat?.checkMark?.hide()
    }

    private fun bindTextColor(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.bindTextColor(msg)
    }

    private fun bindIcon(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.bindIcon(msg)
    }

    private fun bindMsgGravity(gravity: Int) {
        bindLayoutGravity(gravity)
        bindGravity(gravity)
        bindLayoutMsgGravity(gravity)
        binding?.tokochatLayoutBodyMessageBubble?.setMsgGravity(gravity)
    }

    private fun paddingRightMsg() {
        binding?.tokochatLayoutBubbleContainer?.let {
            it.setPadding(
                0, it.paddingTop, bubbleToScreenMargin.toInt(), it.paddingBottom
            )
        }
    }

    private fun paddingLeftMsg() {
        binding?.tokochatLayoutBubbleContainer?.let {
            it.setPadding(
                bubbleToScreenMargin.toInt(), it.paddingTop, 0, it.paddingBottom
            )
        }
    }

    private fun bindMessageInfo(msg: TokoChatMessageBubbleBaseUiModel) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.bindInfo(msg)
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
        if (message.isSender) {
            binding?.tokochatLayoutBubbleContainer?.setMargin(0, topMarginOpposite.toInt(), 0, 0)
        } else {
            binding?.tokochatLayoutBubbleContainer?.setMargin(0, 0, 0, 0)
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
        binding?.tokochatLayoutBubbleContainer?.gravity = gravity
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.tokochatLayoutBubbleContainer?.layoutParams as FrameLayout.LayoutParams
        containerLp.gravity = gravity
        binding?.tokochatLayoutBubbleContainer?.layoutParams = containerLp
    }

    private fun bindLayoutMsgGravity(gravity: Int) {
        binding?.tokochatLayoutBodyMessageContainer?.gravity = gravity
    }

    private fun bindBackground(drawable: Drawable?) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.background = drawable
    }

    companion object {
        val LAYOUT = R.layout.tokochat_item_message_bubble
    }
}
