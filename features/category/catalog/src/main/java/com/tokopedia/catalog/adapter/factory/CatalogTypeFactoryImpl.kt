package com.tokopedia.catalog.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.viewholder.products.CatalogListProductViewHolder
import com.tokopedia.catalog.viewholder.products.CatalogListShimmerModel
import com.tokopedia.catalog.viewholder.products.CatalogListShimmerViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.BaseProductTypeFactoryImpl
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.viewholders.shimmer.ListShimmerViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalog.ListCatalogCardViewHolder

class CatalogTypeFactoryImpl(private var catalogProductCardListener : CatalogProductCardListener) : BaseProductTypeFactoryImpl(), CatalogTypeFactory {

    override fun type(listShimmerModel: CatalogListShimmerModel): Int {
        return CatalogListShimmerViewHolder.LAYOUT
    }


    override fun type(catalogItem: CatalogProductItem): Int {
        when (getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> return CatalogListProductViewHolder.LAYOUT
            else -> return CatalogListShimmerViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            CatalogListShimmerViewHolder.LAYOUT -> viewHolder = CatalogListShimmerViewHolder(view)
            CatalogListProductViewHolder.LAYOUT -> viewHolder = CatalogListProductViewHolder(view, catalogProductCardListener)
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder

    }
}
