package com.tokopedia.checkout.view.feature.cartlist.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartTickerHolderData
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_cart_ticker.view.*

class CartTickerViewHolder(itemView: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(cartTickerData: CartTickerHolderData) {
        itemView.cartTicker.tickerType = Ticker.TYPE_ANNOUNCEMENT
        itemView.cartTicker.tickerShape = Ticker.SHAPE_FULL
        itemView.cartTicker.closeButtonVisibility = View.GONE
        itemView.cartTicker.setHtmlDescription(cartTickerData.message)

        // Workaround for ticker not wrapping multiline content correctly
        itemView.cartTicker.post {
            itemView.cartTicker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            itemView.cartTicker.requestLayout()
        }

        actionListener.onShowCartTicker(cartTickerData.id)
    }

    companion object {
        val LAYOUT = R.layout.item_cart_ticker
    }
}