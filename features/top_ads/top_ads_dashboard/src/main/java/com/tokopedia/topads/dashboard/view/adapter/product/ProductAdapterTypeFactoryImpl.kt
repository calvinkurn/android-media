package com.tokopedia.topads.dashboard.view.adapter.product

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewholder.ProductViewHolder
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.product.viewmodel.ProductItemModel

class ProductAdapterTypeFactoryImpl(var onSwitchAction: ((pos: Int, isChecked: Boolean) -> Unit),
                                    var onSelectMode: ((select:Boolean) -> Unit)) : ProductAdapterTypeFactory {

    override fun type(model: ProductItemModel) = ProductItemViewHolder.LAYOUT

    override fun type(model: ProductEmptyModel) = ProductEmptyViewHolder.LAYOUT

    override fun holder(type: Int, view: View): ProductViewHolder<*> {
        return when (type) {
            ProductEmptyViewHolder.LAYOUT -> ProductEmptyViewHolder(view)
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view, onSwitchAction, onSelectMode)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}