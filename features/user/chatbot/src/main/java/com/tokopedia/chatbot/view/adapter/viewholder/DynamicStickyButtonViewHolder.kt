package com.tokopedia.chatbot.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicContentCode105Binding
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.DynamicStickyButtonListener
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
        return ViewUtil.generateBackgroundWithShadow(
            binding?.customChatLayoutContainer,
            R.color.chatbot_dms_left_message_bg,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
        )
    }

    override fun getCustomChatLayoutId(): Int = R.id.chatbot_tv_message_content

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_content_code_105
    }
}

private fun LinearLayout.setContainerBackground(bg: Drawable?) {
    val pl = paddingLeft
    val pt = paddingTop
    val pr = paddingRight
    val pb = paddingBottom
    background = bg
    setPadding(pl, pt, pr, pb)
}
