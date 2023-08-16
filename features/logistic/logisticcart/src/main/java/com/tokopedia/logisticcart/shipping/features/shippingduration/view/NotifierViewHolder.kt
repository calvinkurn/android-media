package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.unifycomponents.ticker.Ticker

class NotifierViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tickerNotifier: Ticker = itemView.findViewById(R.id.ticker_notifier)

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_notifier
    }

    fun bindData(data: NotifierModel) {
        tickerNotifier.setTextDescription(data.text)
    }
}
