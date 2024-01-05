package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemMessageBubbleBinding
import com.tokopedia.tokochat_common.databinding.TokochatPartialMessageBubbleBinding
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder.generateLeftBg
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder.generateRightBg
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatMessageBubbleListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatMessageBubbleUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class TokoChatMessageBubbleViewHolder(
    itemView: View,
    private val bubbleMessageBubbleListener: TokoChatMessageBubbleListener
): BaseViewHolder(itemView) {

    private val binding: TokochatItemMessageBubbleBinding? by viewBinding()
    private val messageBubbleBinding: TokochatPartialMessageBubbleBinding? by viewBinding()

    private val bubbleToScreenMargin: Float = itemView.context?.resources?.getDimension(
        com.tokopedia.unifyprinciples.R.dimen.unify_space_12
    ) ?: 0f

    private val bgLeft = generateLeftBg(messageBubbleBinding?.tokochatLayoutMessageChat)
    private val bgRight = generateRightBg(messageBubbleBinding?.tokochatLayoutMessageChat)

    fun bind(msg: TokoChatMessageBubbleUiModel) {
        TokoChatMessageBubbleViewHolderBinder.bindChatMessage(
            msg, messageBubbleBinding?.tokochatLayoutMessageChat)
        TokoChatMessageBubbleViewHolderBinder.bindHour(
            msg, messageBubbleBinding?.tokochatLayoutMessageChat)
        bindMargin(msg)
        bindClick()
        bindTextColor(msg)
        bindMessageReadMore(msg)
        if (msg.isSender) {
            setSenderMessageLayout(msg)
        } else {
            setReceiverLayout()
        }
    }

    /**
     * Right side message
     */
    private fun setSenderMessageLayout(msg: TokoChatMessageBubbleUiModel) {
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

    private fun bindTextColor(msg: TokoChatMessageBubbleUiModel) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.bindTextColor(msg)
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

    private fun bindMessageReadMore(msg: TokoChatMessageBubbleUiModel) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.bindReadMore(msg) {
            bubbleMessageBubbleListener.onClickReadMore(msg)
        }
    }

    private fun bindMargin(message: TokoChatMessageBubbleUiModel) {
        if (message.isSender) {
            binding?.tokochatLayoutBubbleContainer?.setMargin(0, 0, 0, 0)
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
        val containerLp = binding?.tokochatLayoutBubbleContainer?.layoutParams as? FrameLayout.LayoutParams
        containerLp?.gravity = gravity
        binding?.tokochatLayoutBubbleContainer?.layoutParams = containerLp
    }

    private fun bindLayoutMsgGravity(gravity: Int) {
        binding?.tokochatLayoutBodyMessageContainer?.gravity = gravity
    }

    private fun bindBackground(drawable: Drawable?) {
        messageBubbleBinding?.tokochatLayoutMessageChat?.background = drawable
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_message_bubble
    }
}
