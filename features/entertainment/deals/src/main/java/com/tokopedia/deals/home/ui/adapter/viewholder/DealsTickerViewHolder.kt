package com.tokopedia.deals.home.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.home.data.DealsEventHome
import com.tokopedia.unifycomponents.ticker.Ticker
import kotlinx.android.synthetic.main.item_deals_ticker.view.*

/**
 * @author by astidhiyaa on 19/05/21
 */

class DealsTickerViewHolder(itemView: View)
    : BaseViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_deals_ticker
    }

    fun bind(tickerData: DealsEventHome.TickerHome){
        with(itemView.dealsTicker){
            setHtmlDescription(tickerData.message)
            tickerType = Ticker.TYPE_WARNING
        }
    }
}