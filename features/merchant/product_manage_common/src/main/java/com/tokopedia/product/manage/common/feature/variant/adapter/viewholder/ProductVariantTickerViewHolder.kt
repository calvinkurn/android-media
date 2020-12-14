package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import kotlinx.android.synthetic.main.item_product_manage_announcement_ticker.view.*

class ProductVariantTickerViewHolder(itemView: View): AbstractViewHolder<ProductVariantTicker>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_manage_announcement_ticker
    }

    override fun bind(data: ProductVariantTicker) {
        setupView()
        setupTicker(data)
    }

    private fun setupTicker(data: ProductVariantTicker) {
        itemView.ticker.apply {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)
        }
    }


    private fun setupView() {
        val verticalMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.spacing_lvl2)
        val horizontalMargin = itemView.context.resources.getDimensionPixelSize(R.dimen.spacing_lvl4)
        itemView.setMargin(horizontalMargin, 0, horizontalMargin, verticalMargin)
    }
}