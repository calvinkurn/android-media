package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariantTicker
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter

class ProductVariantTickerViewHolder(itemView: View) : AbstractViewHolder<ProductVariantTicker>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_manage_ticker
    }

    override fun bind(data: ProductVariantTicker) {
        initView {
            setupView()
            setupTicker(data)
        }
    }

    private fun setupTicker(data: ProductVariantTicker) {
        itemView.findViewById<Ticker>(R.id.ticker).apply {
            val tickerList = data.tickerList
            val adapter = TickerPagerAdapter(context, tickerList)
            addPagerView(adapter, tickerList)

            /*
            This is a temporary fix for cropped ticker unify issue which happened only on recyclerview.
            The issue happened because the view pager didn't redrawn its views when attached in recyclerview.
            The actual fix will be done inside the Ticker component and expected to be ready in 1.1.71 or 1.1.72 unify components version.
            */
            post {
                (this.getChildAt(0) as? CardView)?.getChildAt(0)?.invalidate()
                (this.getChildAt(0) as? CardView)?.getChildAt(0)?.requestLayout()
            }
        }
    }


    private fun setupView() {
        val verticalSpacing = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
        val horizontalSpacing = itemView.context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
        itemView.setMargin(horizontalSpacing, 0, horizontalSpacing, verticalSpacing)
    }

    private fun initView(block: () -> Unit) {
        itemView.findViewById<Ticker>(R.id.ticker).post {
            block.invoke()
        }
    }
}