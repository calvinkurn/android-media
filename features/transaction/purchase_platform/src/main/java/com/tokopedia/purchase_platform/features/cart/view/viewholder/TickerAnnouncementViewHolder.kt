package com.tokopedia.purchase_platform.features.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.view.common.TickerAnnouncementActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_cart_ticker.view.*

class TickerAnnouncementViewHolder(itemView: View, val actionListener: TickerAnnouncementActionListener?) : RecyclerView.ViewHolder(itemView) {

    fun bind(tickerAnnouncementData: TickerAnnouncementHolderData) {
        itemView.cartTicker.tickerType = Ticker.TYPE_ANNOUNCEMENT
        itemView.cartTicker.tickerShape = Ticker.SHAPE_FULL
        itemView.cartTicker.closeButtonVisibility = View.GONE
        itemView.cartTicker.setHtmlDescription(tickerAnnouncementData.message)

        // Workaround for ticker not wrapping multiline content correctly
        itemView.cartTicker.post {
            itemView.cartTicker.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            itemView.cartTicker.requestLayout()
        }

        actionListener?.onShowCartTicker(tickerAnnouncementData.id)
    }

    companion object {
        val LAYOUT = R.layout.item_ticker_anouncement
    }
}