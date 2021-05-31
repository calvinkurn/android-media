package com.tokopedia.checkout.view.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementActionListener
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder

class ShipmentTickerAnnouncementViewHolder(itemView: View, actionListener: TickerAnnouncementActionListener?): TickerAnnouncementViewHolder(itemView, actionListener) {

    internal var data: TickerAnnouncementHolderData = TickerAnnouncementHolderData()

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