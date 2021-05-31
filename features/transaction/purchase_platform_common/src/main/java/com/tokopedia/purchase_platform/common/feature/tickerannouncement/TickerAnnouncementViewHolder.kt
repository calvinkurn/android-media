package com.tokopedia.purchase_platform.common.feature.tickerannouncement

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.common.R
import com.tokopedia.unifycomponents.ticker.Ticker

open class TickerAnnouncementViewHolder(itemView: View, val actionListener: TickerAnnouncementActionListener?) : RecyclerView.ViewHolder(itemView) {

    val cartTicker: Ticker? = itemView.findViewById(R.id.cartTicker)

    open fun bind(tickerAnnouncementData: TickerAnnouncementHolderData) {
        cartTicker?.tickerType = Ticker.TYPE_ANNOUNCEMENT
        cartTicker?.tickerShape = Ticker.SHAPE_FULL
        cartTicker?.closeButtonVisibility = View.GONE
        cartTicker?.setHtmlDescription(tickerAnnouncementData.message)

        // Workaround for ticker not wrapping multiline content correctly
        cartTicker?.post {
            cartTicker?.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            cartTicker?.requestLayout()
        }

        // todo: implement error ticker here? Or new view holder?
        actionListener?.onShowCartTicker(tickerAnnouncementData.id)
    }

    companion object {
        val LAYOUT = R.layout.item_ticker_anouncement
    }
}