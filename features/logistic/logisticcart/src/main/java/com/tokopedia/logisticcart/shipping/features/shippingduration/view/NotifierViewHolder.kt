package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.databinding.ItemNotifierBinding
import com.tokopedia.logisticcart.shipping.model.NotifierModel

class NotifierViewHolder(private val binding: ItemNotifierBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val LAYOUT = 2
    }

    fun bindData(data: NotifierModel) {
        if (data.text.isNotEmpty()) {
            binding.tickerNotifier.setTextDescription(data.text)
        }
    }
}
