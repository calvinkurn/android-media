package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.common.databinding.LayoutProductManageTickerBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.CampaignStockTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.utils.view.binding.viewBinding

class CampaignStockTickerViewHolder(
    itemView: View?
) : AbstractViewHolder<CampaignStockTickerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.product.manage.common.R.layout.layout_product_manage_ticker
    }

    private val binding by viewBinding<LayoutProductManageTickerBinding>()

    private val ticker: Ticker?
        get() = binding?.ticker

    override fun bind(data: CampaignStockTickerUiModel) {
        initView {
            setupLayout()
            setupTicker(data)
        }
    }

    private fun setupTicker(data: CampaignStockTickerUiModel) {
        ticker?.run {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)
        }
    }

    private fun setupLayout() {
        ticker?.run {
            val topMargin = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            val horizontalMargin = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            setMargin(horizontalMargin, topMargin, horizontalMargin, 0)
        }
    }

    private fun initView(block: () -> Unit) {
        ticker?.post {
            block.invoke()
        }
    }
}