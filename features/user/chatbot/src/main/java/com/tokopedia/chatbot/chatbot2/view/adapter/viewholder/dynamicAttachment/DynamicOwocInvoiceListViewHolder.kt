package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.owocinvoice.DynamicOwocInvoicePojo
import com.tokopedia.chatbot.databinding.ItemChatbotOwocInvoiceBinding
import com.tokopedia.kotlin.extensions.view.hide
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
                textProductDescription.text = productDescription
                if (shopBadgeImageUrl?.isEmpty() == true) {
                    shopBadge.hide()
                }
            }
        }
    }
}
