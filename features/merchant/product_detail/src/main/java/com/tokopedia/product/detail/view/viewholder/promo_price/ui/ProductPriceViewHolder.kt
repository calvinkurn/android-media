package com.tokopedia.product.detail.view.viewholder.promo_price.ui

import android.view.View
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ProductPromoPriceViewHolderBinding
import com.tokopedia.product.detail.view.fragment.delegate.GoToApplink
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.product.detail.view.viewholder.promo_price.delegate.ProductPriceCallback

class ProductPriceViewHolder(
    view: View,
    private val callback: ProductPriceCallback
) : ProductDetailPageViewHolder<ProductPriceUiModel>(view) {

    private val binding by lazyThreadSafetyNone {
        ProductPromoPriceViewHolderBinding.bind(view)
    }

    companion object {
        val LAYOUT = R.layout.product_promo_price_view_holder
    }

    override fun bind(element: ProductPriceUiModel) {
        binding.pdpPromoPriceComposeView.setContent {
            ProductDetailPriceComponent(
                promoPriceData = element.promoPriceData,
                normalPromoUiModel = element.normalPromoUiModel,
                priceComponentType = element.priceComponentType,
                normalPriceBoUrl = element.normalPriceBoUrl,
                onPromoPriceClicked = {
                    element.promoPriceData?.applink?.let {
                        callback.event(GoToApplink(it))
                    }
                }
            )
        }
    }
}