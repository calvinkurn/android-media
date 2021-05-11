package com.tokopedia.purchase_platform.common.feature.tickerannouncement

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_ticker_anouncement.view.*

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

        // todo: implement error ticker here? Or new view holder?
        actionListener?.onShowCartTicker(tickerAnnouncementData.id)
    }

    companion object {
        val LAYOUT = R.layout.item_ticker_anouncement
    }
}