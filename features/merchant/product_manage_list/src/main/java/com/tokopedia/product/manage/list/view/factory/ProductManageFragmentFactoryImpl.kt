package com.tokopedia.product.manage.list.view.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.list.view.adapter.ProductManageEmptyList
import com.tokopedia.product.manage.list.view.adapter.ProductManageListViewHolder
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel

class ProductManageFragmentFactoryImpl(val listener: BaseCheckableViewHolder.CheckableInteractionListener, private val clickOptionListenerListener: ProductManageListViewHolder.ProductManageViewHolderListener) : BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<ProductManageViewModel> {

    override fun type(postalCodeViewModel: ProductManageViewModel): Int = ProductManageListViewHolder.LAYOUT

    override fun type(viewModel: EmptyModel?): Int =  ProductManageEmptyList.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return if (type == ProductManageListViewHolder.LAYOUT) {
            ProductManageListViewHolder(view, listener, clickOptionListenerListener)
        } else if (type == ProductManageEmptyList.LAYOUT) {
            ProductManageEmptyList(view)
        } else {
            super.createViewHolder(view, type)
        }
    }
}