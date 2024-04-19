package com.tokopedia.topads.view.adapter.product

import android.view.View
import com.tokopedia.topads.view.adapter.product.viewholder.KeyWordItemViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductEmptyViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductItemViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductShimmerViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.KeyWordItemUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemUiModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerUiModel

class ProductListAdapterTypeFactoryImpl(var actionSelected: (() -> Unit)?) : ProductListAdapterTypeFactory {

    override fun type(model: ProductItemUiModel): Int = ProductItemViewHolder.LAYOUT

    override fun type(model: ProductEmptyUiModel): Int = ProductEmptyViewHolder.LAYOUT

    override fun type(model: ProductShimmerUiModel): Int = ProductShimmerViewHolder.LAYOUT

    override fun type(model: KeyWordItemUiModel): Int = KeyWordItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): ProductViewHolder<*> {
        return when(type){
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view, actionSelected)
            KeyWordItemViewHolder.LAYOUT -> KeyWordItemViewHolder(view)
            ProductEmptyViewHolder.LAYOUT -> ProductEmptyViewHolder(view)
            ProductShimmerViewHolder.LAYOUT -> ProductShimmerViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}
