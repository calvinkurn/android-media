package com.tokopedia.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemTickerShippingCompletionBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShippingCompletionTickerModel

class ShippingCompletionTickerViewHolder(private val binding: ItemTickerShippingCompletionBinding, private val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_TICKER_SHIPPING_COMPLETION = R.layout.item_ticker_shipping_completion
    }

    fun bindViewHolder(tickerModel: ShippingCompletionTickerModel) {
        actionListener.onShowTickerShippingCompletion()
        binding.tickerShippingCompletion.setTextDescription(tickerModel.tickerMessage)
        binding.labelButtonCheckShippingCompletion.setOnClickListener { actionListener.onCheckShippingCompletionClicked() }
    }
}
