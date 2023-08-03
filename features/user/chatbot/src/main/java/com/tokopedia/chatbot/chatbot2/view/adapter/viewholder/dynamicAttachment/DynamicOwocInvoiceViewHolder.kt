package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.data.owocinvoice.DynamicOwocInvoicePojo
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotDynamicOwocInvoiceAdapter
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.BaseChatBotViewHolder
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotOwocListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackground
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicOwocBinding
import com.tokopedia.chatbot.util.setContainerBackground
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class DynamicOwocInvoiceViewHolder(
    itemView: View,
    private val chatLinkHandlerListener: ChatLinkHandlerListener,
    private val listener: ChatbotOwocListener
) : BaseChatBotViewHolder<DynamicOwocInvoiceUiModel>(itemView) {

    private var binding: ItemChatbotDynamicOwocBinding? by viewBinding()

    private val movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    private var invoiceAdapter: ChatbotDynamicOwocInvoiceAdapter? = null
    private var showSeeAll: Boolean = true
    private var modifiedInvoiceList: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>? = emptyList()

    override fun bind(uiModel: DynamicOwocInvoiceUiModel) {
        super.bind(uiModel)
        verifyReplyTime(uiModel)
        ChatbotMessageViewHolderBinder.bindChatMessage(
            uiModel.message,
            customChatLayout,
            movementMethod
        )

        binding?.apply {
            customChatLayout.apply {
                showTimeStamp(false)
                background = null
                message?.text = uiModel.message
            }
            invoiceBubbleTextView.text = ChatbotMessageViewHolderBinder.getTime(uiModel.replyTime)
            invoiceBubbleTextView.show()
            messageParent.setContainerBackground(bindBackground())
            invoiceAdapter = ChatbotDynamicOwocInvoiceAdapter()
            setUpReadMoreButtonClickListener(uiModel)
            if ((uiModel.invoiceList?.size ?: 0) > INVOICE_LIST_SIZE) {
                showSeeAll = true
                setupReadMoreButton()
                btnReadMore.root.show()
                modifiedInvoiceList = uiModel.invoiceList?.take(INVOICE_LIST_SIZE)
                initializeAdapter(modifiedInvoiceList)
            } else {
                showSeeAll = false
                btnReadMore.root.hide()
                initializeAdapter(uiModel.invoiceList)
            }
        }
    }

    private fun ItemChatbotDynamicOwocBinding.setUpReadMoreButtonClickListener(uiModel: DynamicOwocInvoiceUiModel) {
        btnReadMore.root.setOnClickListener {
            showSeeAll = !showSeeAll
            if (showSeeAll) {
                modifiedInvoiceList = uiModel.invoiceList?.take(INVOICE_LIST_SIZE)
                initializeAdapter(modifiedInvoiceList)
            } else {
                initializeAdapter(uiModel.invoiceList)
            }
            setupReadMoreButton()
        }
    }

    private fun ItemChatbotDynamicOwocBinding.setupReadMoreButton() {
        if (showSeeAll) {
            btnReadMore.readMoreOptions.text =
                itemView.context.resources.getString(R.string.chatbot_invoice_owoc_see_all)
            btnReadMore.arrowUpDown.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_chevron_down)
        } else {
            btnReadMore.readMoreOptions.text =
                itemView.context.resources.getString(R.string.chatbot_invoice_owoc_hide)
            btnReadMore.arrowUpDown.setImageResource(com.tokopedia.iconunify.R.drawable.iconunify_chevron_up)
        }
    }

    private fun bindBackground(): Drawable? {
        return generateLeftMessageBackground(
            binding?.messageParent,
            R.color.chatbot_dms_left_message_bg,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20
        )
    }

    private fun ItemChatbotDynamicOwocBinding.initializeAdapter(invoiceList: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>?) {
        rvInvoiceList.apply {
            layoutManager = LinearLayoutManager(context)
            if (invoiceList != null) {
                invoiceAdapter?.setList(invoiceList)
            }
            adapter = invoiceAdapter
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chatbot_dynamic_owoc
        const val INVOICE_LIST_SIZE = 2
    }
}
