package com.tokopedia.purchase_platform.features.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.view.uimodel.TickerShippingCompletionModel
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.unifycomponents.ticker.Ticker

class ShippingCompletionTickerViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(view) {

    private val ticker by lazy {
        view.findViewById<Ticker>(R.id.ticker_shipping_completion)
    }

    companion object {
        @JvmStatic
        val ITEM_VIEW_TICKER_SHIPPING_COMPLETION = R.layout.item_ticker_shipping_completion
    }

    fun bindViewHolder(model: TickerShippingCompletionModel) {
        ticker.setTextDescription(model.text)
    }

}