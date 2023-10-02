package com.tokopedia.checkout.revamp.view.viewholder

import android.view.View
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder

class CheckoutTickerViewHolder(itemView: View) : TickerAnnouncementViewHolder(itemView) {

    internal var data: TickerAnnouncementHolderData = TickerAnnouncementHolderData()

    fun bind(ticker: CheckoutTickerModel) {
        bind(ticker.ticker)
    }

    override fun bind(tickerAnnouncementData: TickerAnnouncementHolderData) {
        data = tickerAnnouncementData
        if (isEmptyTicker()) {
            cartTicker?.gone()
        } else {
            super.bind(tickerAnnouncementData)
            cartTicker?.visible()
        }
    }

    internal fun isEmptyTicker(): Boolean {
        return data.message.isEmpty()
    }
}
