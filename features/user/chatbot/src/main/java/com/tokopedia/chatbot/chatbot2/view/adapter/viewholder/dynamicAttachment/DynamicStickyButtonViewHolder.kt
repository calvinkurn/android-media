package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.BaseChatBotViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.DynamicStickyButtonListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode105Binding
import com.tokopedia.chatbot.util.setContainerBackground
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.utils.view.binding.viewBinding

class DynamicStickyButtonViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val dynamicStickyButtonListener: DynamicStickyButtonListener
) :
    BaseChatBotViewHolder<DynamicStickyButtonUiModel>(itemView) {

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private var binding: ItemChatbotDynamicContentCode105Binding? by viewBinding()

    override fun bind(uiModel: DynamicStickyButtonUiModel) {
        super.bind(uiModel)
        verifyReplyTime(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            uiModel.contentText,
            customChatLayout,
            movementMethod
        )
        ChatbotMessageViewHolderBinder.bindHour(uiModel.replyTime, customChatLayout)
        bindBackground()

        if (uiModel.status != ChatbotConstant.RENDER_TO_UI_BASED_ON_STATUS) {
            binding?.customChatLayoutContainer?.hide()
        } else {
            binding?.apply {
                binding?.customChatLayoutContainer?.setContainerBackground(bindBackground())
                chatbotTvMessageContent.message?.text = uiModel.contentText
                chatbotTvMessageContent.background = null
                actionButton.text = uiModel.actionBubble.text
                actionButton.setOnClickListener {
                    dynamicStickyButtonListener.onButtonActionClicked(
                        uiModel.actionBubble
                    )
                }
            }
        }
    }

    private fun bindBackground(): Drawable? {
        return generateLeftMessageBackground(
            binding?.customChatLayoutContainer,
            R.color.chatbot_dms_left_message_bg,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
        )
    }

    override fun getCustomChatLayoutId(): Int = R.id.chatbot_tv_message_content

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_content_code_105
    }
}
