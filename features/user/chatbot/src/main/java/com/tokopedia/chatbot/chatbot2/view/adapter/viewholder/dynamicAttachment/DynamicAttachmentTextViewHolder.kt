package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.BaseChatBotViewHolder
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.toMessageUiModel
import com.tokopedia.chatbot.chatbot2.view.util.ChatBackground
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.generateRightMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode106Binding
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding

class DynamicAttachmentTextViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val replyBubbleListener: ReplyBubbleAreaMessage.Listener
) :
    BaseChatBotViewHolder<DynamicAttachmentTextUiModel>(itemView) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private var binding: ItemChatbotDynamicContentCode106Binding? by viewBinding()
    override fun bind(uiModel: DynamicAttachmentTextUiModel) {
        super.bind(uiModel)
        verifyReplyTime(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            uiModel.message,
            binding?.messageLayoutWithReply?.fxChat,
            movementMethod,
            uiModel.isSender
        )
        ChatbotMessageViewHolderBinder.bindHour(
            uiModel.replyTime,
            binding?.messageLayoutWithReply?.fxChat
        )
        ChatbotMessageViewHolderBinder.bindChatReadStatus(
            uiModel,
            binding?.messageLayoutWithReply
        )
        ChatBackground.bindBackground(binding?.chatLayoutContainer, uiModel.isSender)

        binding?.apply {
            messageLayoutWithReply.fxChat?.message?.text = uiModel.message
            messageLayoutWithReply.replyBubbleContainer?.hide()
            messageLayoutWithReply.background = bindBackground(uiModel)
            messageLayoutWithReply.fxChat?.setOnLongClickListener {
                replyBubbleListener.showReplyOption(
                    uiModel.toMessageUiModel(),
                    messageLayoutWithReply.fxChat?.message
                )
                return@setOnLongClickListener true
            }
            messageLayoutWithReply.fxChat?.message?.setOnLongClickListener {
                replyBubbleListener.showReplyOption(uiModel.toMessageUiModel(), messageLayoutWithReply.fxChat?.message)
                return@setOnLongClickListener true
            }
        }
    }

    private fun bindBackground(uiModel: DynamicAttachmentTextUiModel): Drawable? {
        setUpGuideline(uiModel)
        return if (uiModel.isSender) {
            generateRightMessageBackground(
                binding?.messageLayoutWithReply
            )
        } else {
            generateLeftMessageBackground(
                binding?.messageLayoutWithReply
            )
        }
    }

    private fun setUpGuideline(uiModel: DynamicAttachmentTextUiModel) {
        if (uiModel.isSender) {
            val params1 = binding?.guidelineStart?.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = GUIDELINE_2
            val params2 = binding?.guidelineEnd?.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = GUIDELINE_4
            binding?.guidelineStart?.layoutParams = params1
            binding?.guidelineEnd?.layoutParams = params2
        } else {
            val params1 = binding?.guidelineStart?.layoutParams as ConstraintLayout.LayoutParams
            params1.guidePercent = GUIDELINE_1
            val params2 = binding?.guidelineEnd?.layoutParams as ConstraintLayout.LayoutParams
            params2.guidePercent = GUIDELINE_3
            binding?.guidelineStart?.layoutParams = params1
            binding?.guidelineEnd?.layoutParams = params2
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_content_code_106
        const val GUIDELINE_1 = 0f
        const val GUIDELINE_2 = 0.2f
        const val GUIDELINE_3 = 0.8f
        const val GUIDELINE_4 = 1f
    }
}
