package com.tokopedia.attachinvoice.view.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.databinding.ItemAttachinvoiceEmptyViewBinding

class EmptyAttachInvoiceViewHolder(private val binding: ItemAttachinvoiceEmptyViewBinding, private val listener: Listener) : AbstractViewHolder<EmptyModel>(binding.root) {

    interface Listener {
        fun hideAttachButton()
        fun getOpponentName(): String
    }

    override fun bind(element: EmptyModel?) {
        if (element == null) return
        bindEmptyText(element)
        listener.hideAttachButton()
    }

    private fun bindEmptyText(element: EmptyModel) {
        val opponentName = listener.getOpponentName()
        if (opponentName.isEmpty()) return
        val emptyText = "Belum ada transaksi dengan $opponentName"
        binding.tpText.text = emptyText
    }

    companion object {
        val LAYOUT = R.layout.item_attachinvoice_empty_view
    }
}