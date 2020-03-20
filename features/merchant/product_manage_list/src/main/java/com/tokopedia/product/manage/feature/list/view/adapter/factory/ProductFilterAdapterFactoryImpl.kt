package com.tokopedia.product.manage.feature.list.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.TabFilterViewHolder
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.TabFilterViewHolder.*
import com.tokopedia.product.manage.feature.list.view.adapter.viewholder.MoreFilterViewHolder
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*

class ProductFilterAdapterFactoryImpl(
    private val listener: ProductFilterListener
): BaseAdapterTypeFactory(), ProductFilterAdapterFactory {

    override fun type(viewModel: FilterViewModel): Int = if(viewModel is MoreFilter) {
        MoreFilterViewHolder.LAYOUT
    } else {
        TabFilterViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when(viewType) {
            MoreFilterViewHolder.LAYOUT -> MoreFilterViewHolder(view, listener)
            TabFilterViewHolder.LAYOUT -> TabFilterViewHolder(view, listener)
            else -> super.createViewHolder(view, viewType)
        }
    }
}