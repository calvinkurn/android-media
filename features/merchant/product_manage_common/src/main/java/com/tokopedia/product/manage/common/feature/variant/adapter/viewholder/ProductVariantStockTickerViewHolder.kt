package com.tokopedia.product.manage.common.feature.variant.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductTicker

class ProductVariantStockTickerViewHolder(
    itemView: View
): AbstractViewHolder<ProductTicker>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_variant_stock_ticker
    }

    override fun bind(ticker: ProductTicker) {}
}