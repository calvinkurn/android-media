package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.R
import kotlinx.android.synthetic.main.item_notifier.view.*

class NotifierViewHolderInstant(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_notifier_instant
    }

    fun bindData() {
        itemView.ticker_notifier.setTextDescription(itemView.context.getString(R.string.label_hardcoded_courier_ticker_instant))
    }
}