package com.tokopedia.deals.ui.home.ui.adapter.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.ui.dataview.DealsTickerDataView
import com.tokopedia.deals.databinding.ItemDealsTickerBinding
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * @author by astidhiyaa on 19/05/21
 */

class DealsTickerViewHolder(itemView: View)
    : BaseViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_deals_ticker
    }

    fun bind(tickerData: DealsTickerDataView){
        val binding = ItemDealsTickerBinding.bind(itemView)
        with(binding.dealsTicker){
            setHtmlDescription(tickerData.message)
            tickerType = Ticker.TYPE_WARNING
        }
    }
}
