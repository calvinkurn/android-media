package com.tokopedia.catalog.ui.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.domain.model.CatalogProductItem

class CatalogProductListAdapterFactoryImpl(
    private val productListListener: ProductListAdapterListener
) : BaseAdapterTypeFactory(), CatalogProductListAdapterFactory {

    override fun type(uiModel: CatalogProductItem): Int {
        return ProductListViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int = EmptyStateViewHolder.LAYOUT

    override fun type(viewModel: LoadingModel?): Int = LoadingViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProductListViewHolder.LAYOUT -> ProductListViewHolder(view, productListListener)
            EmptyStateViewHolder.LAYOUT -> EmptyStateViewHolder(view)
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}
