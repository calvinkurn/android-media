package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_INSTAN
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_DEFAULT
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_SAMEDAY
import com.tokopedia.unifycomponents.ticker.Ticker

class NotifierViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val tickerNotifier: Ticker = itemView.findViewById(R.id.ticker_notifier)

    companion object {
        @JvmStatic val LAYOUT = R.layout.item_notifier
    }

    fun bindData(data: NotifierModel) {
        val notifierText = setNotifierText(data.type)
        tickerNotifier.setTextDescription(notifierText)
    }

    private fun setNotifierText(type: Int) : String {
        return when(type) {
            TYPE_DEFAULT -> {
                itemView.context.getString(R.string.label_hardcoded_courier_ticker_logistic)
            }
            TYPE_INSTAN -> {
                itemView.context.getString(R.string.label_hardcoded_courier_ticker_instant)
            }
            TYPE_SAMEDAY -> {
                itemView.context.getString(R.string.label_hardcoded_courier_ticker_same_day)
            }
            else -> {
                ""
            }
        }
    }
}