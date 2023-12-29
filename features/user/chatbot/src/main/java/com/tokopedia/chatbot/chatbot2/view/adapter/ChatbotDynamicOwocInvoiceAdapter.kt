package com.tokopedia.chatbot.chatbot2.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.owocinvoice.DynamicOwocInvoicePojo
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.dynamicAttachment.DynamicOwocInvoiceListViewHolder
import com.tokopedia.chatbot.databinding.ItemChatbotOwocInvoiceBinding

class ChatbotDynamicOwocInvoiceAdapter: RecyclerView.Adapter<DynamicOwocInvoiceListViewHolder>() {

    private val data = ArrayList<DynamicOwocInvoicePojo.InvoiceCardOwoc>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DynamicOwocInvoiceListViewHolder {
        val holder = ItemChatbotOwocInvoiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DynamicOwocInvoiceListViewHolder(holder.root)
    }

    override fun onBindViewHolder(holder: DynamicOwocInvoiceListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setList(invoiceList: List<DynamicOwocInvoicePojo.InvoiceCardOwoc>) {
        data.clear()
        data.addAll(invoiceList)
        notifyItemRangeInserted(0, invoiceList.size)
    }
}
