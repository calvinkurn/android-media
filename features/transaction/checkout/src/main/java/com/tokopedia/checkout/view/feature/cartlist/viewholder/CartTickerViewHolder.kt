package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.datamodel.cartlist.CartTickerData
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_cart_ticker.view.*

class CartTickerViewHolder(itemView: View, val rv: RecyclerView?, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(cartTickerData: CartTickerData) {
        itemView.cartTicker.tickerType = Ticker.TYPE_ANNOUNCEMENT
        itemView.cartTicker.tickerShape = Ticker.SHAPE_FULL
        itemView.cartTicker.closeButtonVisibility = View.GONE
        itemView.cartTicker.setHtmlDescription(cartTickerData.message)
        itemView.cartTicker.visibility = View.VISIBLE

        itemView.cartTicker.requestLayout()

        val view = itemView.cartTicker.findViewById<View>(com.tokopedia.unifycomponents.R.id.ticker_description)
        view?.requestLayout()

        if (rv != null) {
            itemView.cartTicker.measure(View.MeasureSpec.makeMeasureSpec(rv.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            itemView.cartTicker.requestLayout()
        }

        actionListener.onShowCartTicker(cartTickerData.id.toString())
    }

    companion object {
        val LAYOUT = R.layout.item_cart_ticker
    }
}