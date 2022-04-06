package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShippingCompletionTickerModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography

class ShippingCompletionTickerViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(view) {

    private val ticker by lazy {
        view.findViewById<Ticker>(R.id.ticker_shipping_completion)
    }
    private val labelButtonCheckShippingCompletion by lazy {
        view.findViewById<Typography>(R.id.label_button_check_shipping_completion)
    }

    companion object {
        @JvmStatic
        val ITEM_VIEW_TICKER_SHIPPING_COMPLETION = R.layout.item_ticker_shipping_completion
    }

    fun bindViewHolder(tickerModel: ShippingCompletionTickerModel) {
        actionListener.onShowTickerShippingCompletion()
        ticker.setTextDescription(tickerModel.tickerMessage)
        labelButtonCheckShippingCompletion.setOnClickListener { actionListener.onCheckShippingCompletionClicked() }
    }

}