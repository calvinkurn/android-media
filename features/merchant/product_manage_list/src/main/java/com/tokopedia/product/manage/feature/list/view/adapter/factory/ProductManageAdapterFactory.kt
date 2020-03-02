package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.ProductViewHolder
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.oldlist.view.adapter.ProductManageEmptyList

class ProductManageAdapterFactory(
    val listener: BaseCheckableViewHolder.CheckableInteractionListener,
    private val viewHolderListener: ProductViewHolder.ProductViewHolderView
) : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<ProductViewModel> {

    override fun type(productViewModel: ProductViewModel): Int = ProductViewHolder.LAYOUT

    override fun type(viewModel: EmptyModel?): Int =  ProductManageEmptyList.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductViewHolder.LAYOUT -> {
                ProductViewHolder(view, listener, viewHolderListener)
            }
            ProductManageEmptyList.LAYOUT -> {
                ProductManageEmptyList(view)
            }
            else -> {
                super.createViewHolder(view, type)
            }
        }
    }
}