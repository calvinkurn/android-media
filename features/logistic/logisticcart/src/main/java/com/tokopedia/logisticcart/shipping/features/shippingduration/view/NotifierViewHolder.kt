package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import kotlinx.android.synthetic.main.item_notifier.view.*

class NotifierViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_notifier
    }

    fun bindData() {

    }

    fun bindInstantOrSamedayCourier() {
        itemView.ticker_notifier.setTextDescription(itemView.context.getString(R.string.label_hardcoded_courier_ticker_logistic))
    }
}