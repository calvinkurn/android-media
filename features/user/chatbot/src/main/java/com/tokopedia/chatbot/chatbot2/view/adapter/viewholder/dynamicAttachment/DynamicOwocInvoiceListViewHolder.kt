package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.owocinvoice.DynamicOwocInvoicePojo
import com.tokopedia.chatbot.databinding.ItemChatbotDynamicOwocBinding
import com.tokopedia.chatbot.databinding.ItemChatbotOwocInvoiceBinding
import com.tokopedia.utils.view.binding.viewBinding

class DynamicOwocInvoiceListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var binding: ItemChatbotOwocInvoiceBinding? by viewBinding()

    fun bind(invoiceDetails: DynamicOwocInvoicePojo.InvoiceCardOwoc) {
        binding?.apply {
            with(invoiceDetails) {
                productImage.urlSrc = productImageUrl ?: ""
                shopBadge.urlSrc = shopBadgeImageUrl ?: ""
                textShopName.text = shopName
                textProductName.text = productName
            }

        }
    }
}
