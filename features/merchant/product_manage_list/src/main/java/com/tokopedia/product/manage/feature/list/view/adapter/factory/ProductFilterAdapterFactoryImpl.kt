package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.FilterTabViewHolder.*
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MoreFilterViewHolder
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*

class ProductFilterAdapterFactoryImpl(
    private val listener: ProductFilterListener
): BaseAdapterTypeFactory(), ProductFilterAdapterFactory {

    override fun type(viewModel: FilterTabViewModel): Int = if(viewModel is MoreFilter) {
        MoreFilterViewHolder.LAYOUT
    } else {
        FilterTabViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            MoreFilterViewHolder.LAYOUT -> MoreFilterViewHolder(view, listener)
            FilterTabViewHolder.LAYOUT -> FilterTabViewHolder(view, listener)
            else -> super.createViewHolder(view, viewType)
        }
    }
}