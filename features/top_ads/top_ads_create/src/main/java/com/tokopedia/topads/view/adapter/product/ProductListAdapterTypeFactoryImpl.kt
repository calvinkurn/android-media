package com.tokopedia.topads.view.adapter.product

import android.view.View
import com.tokopedia.topads.view.adapter.product.viewholder.ProductEmptyViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductItemViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductShimmerViewHolder
import com.tokopedia.topads.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductEmptyViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductShimmerViewModel

class ProductListAdapterTypeFactoryImpl(var actionSelected: (() -> Unit)?) : ProductListAdapterTypeFactory {

    override fun type(model: ProductItemViewModel): Int = ProductItemViewHolder.LAYOUT

    override fun type(model: ProductEmptyViewModel): Int = ProductEmptyViewHolder.LAYOUT

    override fun type(model: ProductShimmerViewModel): Int = ProductShimmerViewHolder.LAYOUT

    override fun holder(type: Int, view: View): ProductViewHolder<*> {
        return when(type){
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view, actionSelected)
            ProductEmptyViewHolder.LAYOUT -> ProductEmptyViewHolder(view)
            ProductShimmerViewHolder.LAYOUT -> ProductShimmerViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}