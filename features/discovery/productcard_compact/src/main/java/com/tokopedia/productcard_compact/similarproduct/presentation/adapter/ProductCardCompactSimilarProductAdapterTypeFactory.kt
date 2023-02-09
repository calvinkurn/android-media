package com.tokopedia.productcard_compact.similarproduct.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.productcard_compact.similarproduct.presentation.viewholder.ProductCardCompactSimilarProductViewHolder

class ProductCardCompactSimilarProductAdapterTypeFactory(
    private val productListener: ProductCardCompactSimilarProductViewHolder.SimilarProductListener?
) : BaseAdapterTypeFactory(), ProductCardCompactSimilarProductTypeFactory {

    override fun type(uiModel: ProductCardCompactSimilarProductUiModel): Int = ProductCardCompactSimilarProductViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductCardCompactSimilarProductViewHolder.LAYOUT -> ProductCardCompactSimilarProductViewHolder(parent, productListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
