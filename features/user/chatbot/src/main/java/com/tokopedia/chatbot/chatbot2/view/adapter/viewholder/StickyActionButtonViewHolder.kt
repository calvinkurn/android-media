package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.unifycomponents.UnifyButton

class StickyActionButtonViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val chatbotAdapterListener: ChatbotAdapterListener,
    private val actionButtonClickListener: StickyActionButtonClickListener
) : BaseChatBotViewHolder<StickyActionButtonUiModel>(itemView) {

    private val customChatLayoutContainer: LinearLayout = itemView.findViewById(
        R.id.custom_chat_layout_container
    )
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private val actionButton: TextView = itemView.findViewById<UnifyButton>(R.id.actionButton)

    private val bg = generateLeftMessageBackground(
        customChatLayoutContainer,
        R.color.chatbot_dms_left_message_bg,
        com.tokopedia.unifyprinciples.R.color.Unify_NN950_20
    )

    override fun bind(viewModel: StickyActionButtonUiModel) {
        bindBackGround()
        verifyReplyTime(viewModel)
        ChatbotMessageViewHolderBinder.bindHour(viewModel.replyTime, customChatLayout)
        setHeaderDate(viewModel)
        hideSenderInfo()
        val senderInfoData = convertToSenderInfo(viewModel.source)
        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition) == true) {
            senderInfoData?.let { bindSenderInfo(it) }
        }
        ChatbotMessageViewHolderBinder.bindChatMessage(
            viewModel.message,
            customChatLayout,
            movementMethod
        )
        setActionButtonTextAndListener(viewModel)
    }

    private fun setActionButtonTextAndListener(viewModel: StickyActionButtonUiModel) {
        actionButton.text = viewModel.stickyActionButton?.firstOrNull()?.text
        actionButton.setOnClickListener {
            val stickyActionButton = viewModel.stickyActionButton?.firstOrNull()
            stickyActionButton?.invoiceRefNum?.let { invoiceRefNum ->
                actionButtonClickListener.onStickyActionButtonClicked(
                    invoiceRefNum,
                    stickyActionButton.replyText
                        ?: ""
                )
            }
        }
    }

    private fun bindBackGround() {
        customChatLayoutContainer.setContainerBackground(bg)
    }

    override fun getCustomChatLayoutId(): Int = R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_sticky_action_buton
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
