package com.tokopedia.chatbot.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton.toMessageUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode106Binding
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.chatbot.view.util.ChatBackground
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
        ChatBackground.bindBackground(binding?.chatLayoutContainer, true)

        binding?.apply {
            messageLayoutWithReply.fxChat?.message?.text = uiModel.message
            messageLayoutWithReply.replyBubbleContainer?.hide()
            messageLayoutWithReply.background = bindBackground()
            messageLayoutWithReply.fxChat?.setOnLongClickListener {
                replyBubbleListener.showReplyOption(uiModel.toMessageUiModel())
                return@setOnLongClickListener true
            }
            messageLayoutWithReply.fxChat?.message?.setOnLongClickListener {
                replyBubbleListener.showReplyOption(uiModel.toMessageUiModel(), messageLayoutWithReply.fxChat?.message)
                return@setOnLongClickListener true
            }
        }
    }

    private fun bindBackground(): Drawable? {
        return ViewUtil.generateBackgroundWithShadow(
            binding?.messageLayoutWithReply,
            R.color.chatbot_dms_right_chat_message_bg,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_content_code_106
    }
}
