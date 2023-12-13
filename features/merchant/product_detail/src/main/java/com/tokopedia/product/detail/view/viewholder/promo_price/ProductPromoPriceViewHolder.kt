package com.tokopedia.product.detail.view.viewholder.promo_price

import android.view.View
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ProductPromoPriceViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder

class ProductPromoPriceViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ProductPromoPriceUiModel>(view) {

    private val binding by lazyThreadSafetyNone {
        ProductPromoPriceViewHolderBinding.bind(view)
    }

    companion object {
        val LAYOUT = R.layout.product_promo_price_view_holder
    }

    override fun bind(element: ProductPromoPriceUiModel) {
        binding.pdpPromoPriceComposeView.setContent {
            ProductDetailPriceComponent(
                element.promoPriceData,
                element.normalPromoUiModel,
                element.priceComponentType
            )
        }
    }
}