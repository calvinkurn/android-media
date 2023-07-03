package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.BaseChatBotViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotDynamicOwocListener
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.owocInvoice.ChatbotDynamicOwocInvoiceAdapter
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicOwocBinding
import com.tokopedia.chatbot.util.setContainerBackground
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class DynamicOwocInvoiceViewHolder(
    itemView: View,
    private val listener: ChatbotDynamicOwocListener,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
) : BaseChatBotViewHolder<DynamicOwocInvoiceUiModel>(itemView) {

    private var binding: ItemChatbotDynamicOwocBinding? by viewBinding()

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private var invoiceAdapter: ChatbotDynamicOwocInvoiceAdapter? = null

    override fun bind(uiModel: DynamicOwocInvoiceUiModel) {
        super.bind(uiModel)

        verifyReplyTime(uiModel)
        binding?.customChatLayout?.showTimeStamp(false)
        binding?.customChatLayout?.background = null
        binding?.mainParent?.setContainerBackground(bindBackground())
        ChatbotMessageViewHolderBinder.bindChatMessage(
            uiModel.message,
            customChatLayout,
            movementMethod
        )
    //    ChatbotMessageViewHolderBinder.bindHourTextView(uiModel, binding?.tvTime)

        binding?.apply {
            customChatLayout.message?.text = uiModel.message
            initializeAdapter(uiModel)
        }
    }

    private fun bindBackground(): Drawable? {
        return generateLeftMessageBackground(
            binding?.mainParent,
            R.color.chatbot_dms_left_message_bg,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20
        )
    }

    private fun ItemChatbotDynamicOwocBinding.initializeAdapter(uiModel: DynamicOwocInvoiceUiModel) {
        invoiceAdapter = ChatbotDynamicOwocInvoiceAdapter(listener)

        rvInvoiceList.apply {
            layoutManager = LinearLayoutManager(context)
            //TODO change this one
            if (uiModel.invoiceList != null) {
                invoiceAdapter?.setList(uiModel.invoiceList!!)
            }

            adapter = invoiceAdapter
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_owoc
    }
}
