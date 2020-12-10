package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

class CampaignStockTickerViewHolder(
    itemView: View?
): AbstractViewHolder<CampaignStockTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_announcement_ticker
    }

    override fun bind(data: CampaignStockTickerUiModel) {
        setupLayout()
        setupTicker(data)
    }

    private fun setupTicker(data: CampaignStockTickerUiModel) {
        itemView.findViewById<Ticker>(R.id.ticker).apply {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)
        }
    }

    private fun setupLayout() {
        val topMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
        val horizontalMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.spacing_lvl3)
        itemView.setMargin(horizontalMargin, topMargin, horizontalMargin, 0)
    }
}