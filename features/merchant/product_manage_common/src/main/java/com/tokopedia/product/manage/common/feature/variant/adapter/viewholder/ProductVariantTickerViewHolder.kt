package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.databinding.LayoutProductManageTickerBinding
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.utils.view.binding.viewBinding

class ProductVariantTickerViewHolder(itemView: View) : AbstractViewHolder<ProductVariantTicker>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_manage_ticker
    }

    private val binding by viewBinding<LayoutProductManageTickerBinding>()

    override fun bind(data: ProductVariantTicker) {
        initView {
            setupView()
            setupTicker(data)
        }
    }

    private fun setupTicker(data: ProductVariantTicker) {
        binding?.ticker?.run {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)
        }
    }


    private fun setupView() {
        val verticalSpacing = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalSpacing = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
        itemView.setMargin(horizontalSpacing, 0, horizontalSpacing, verticalSpacing)
    }

    private fun initView(block: () -> Unit) {
        binding?.ticker?.post {
            block.invoke()
        }
    }
}
