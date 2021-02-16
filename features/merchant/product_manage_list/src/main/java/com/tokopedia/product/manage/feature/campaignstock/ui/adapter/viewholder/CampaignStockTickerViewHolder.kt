package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

class CampaignStockTickerViewHolder(
    itemView: View?
) : AbstractViewHolder<CampaignStockTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.product.manage.common.R.layout.layout_product_manage_ticker
    }

    override fun bind(data: CampaignStockTickerUiModel) {
        initView {
            setupLayout()
            setupTicker(data)
        }
    }

    private fun setupTicker(data: CampaignStockTickerUiModel) {
        itemView.findViewById<Ticker>(com.tokopedia.product.manage.common.R.id.ticker).apply {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)
        }
    }

    private fun setupLayout() {
        itemView.findViewById<Ticker>(com.tokopedia.product.manage.common.R.id.ticker).apply {
            val topMargin = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            val horizontalMargin = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            setMargin(horizontalMargin, topMargin, horizontalMargin, 0)
        }
    }

    private fun initView(block: () -> Unit) {
        itemView.findViewById<Ticker>(com.tokopedia.product.manage.common.R.id.ticker).post {
            block.invoke()
        }
    }
}