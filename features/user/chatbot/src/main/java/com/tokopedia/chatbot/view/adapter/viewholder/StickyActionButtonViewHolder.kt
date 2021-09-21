package com.tokopedia.chatbot.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.unifycomponents.UnifyButton

class StickyActionButtonViewHolder(itemView: View,
                                   private val chatLinkHandlerListener: ChatLinkHandlerListener,
                                   private val chatbotAdapterListener: ChatbotAdapterListener,
                                   private val actionButtonClickListener: StickyActionButtonClickListener) : BaseChatBotViewHolder<StickyActionButtonViewModel>(itemView) {

    private val customChatLayoutContainer: LinearLayout = itemView.findViewById(R.id.custom_chat_layout_container)
    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private val actionButton: TextView = itemView.findViewById<UnifyButton>(R.id.actionButton)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayoutContainer,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    override fun bind(viewModel: StickyActionButtonViewModel) {
        bindBackGround()
        verifyReplyTime(viewModel)
        ChatbotMessageViewHolderBinder.bindHour(viewModel.replyTime, customChatLayout)
        setHeaderDate(viewModel)
        hideSenderInfo()
        val senderInfoData = convertToSenderInfo(viewModel.source)
        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition) == true) {
            senderInfoData?.let { bindSenderInfo(it) }
        }
        ChatbotMessageViewHolderBinder.bindChatMessage(viewModel.message, customChatLayout, movementMethod)
        setActionButtonTextAndListener(viewModel)
    }

    private fun setActionButtonTextAndListener(viewModel: StickyActionButtonViewModel) {
        actionButton.text = viewModel.stickyActionButton?.firstOrNull()?.text
        actionButton.setOnClickListener {
            val stickyActionButton = viewModel.stickyActionButton?.firstOrNull()
            stickyActionButton?.invoiceRefNum?.let { invoiceRefNum ->
                actionButtonClickListener.onStickyActionButtonClicked(invoiceRefNum, stickyActionButton.replyText
                        ?: "")
            }
        }
    }

    private fun bindBackGround() {
        customChatLayoutContainer.setContainerBackground(bg)
    }

    override fun getCustomChatLayoutId(): Int = com.tokopedia.chatbot.R.id.customChatLayout
    override fun getSenderAvatarId(): Int = R.id.senderAvatar
    override fun getSenderNameId(): Int = R.id.senderName
    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.sticky_action_buton_chat_layout
    }
}

private fun LinearLayout.setContainerBackground(bg: Drawable?) {
    val pl = paddingLeft
    val pt = paddingTop
    val pr = paddingRight
    val pb = paddingBottom
    setBackground(bg)
    setPadding(pl, pt, pr, pb)
}
