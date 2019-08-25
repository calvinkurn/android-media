package com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.BigGridCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.ListCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.SmallGridCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogItem
import com.tokopedia.discovery.categoryrevamp.data.productModel.typefactory.BaseProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CatalogCardListener

class CatalogTypeFactoryImpl(val catalogCardListener: CatalogCardListener) : BaseProductTypeFactoryImpl(), CatalogTypeFactory {

    override fun type(catalogItem: CatalogItem): Int {
        when (getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> return ListCatalogCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_1 -> return BigGridCatalogCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_2 -> return SmallGridCatalogCardViewHolder.LAYOUT
            else -> return SmallGridCatalogCardViewHolder.LAYOUT
        }
    }


    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {

        val viewHolder: AbstractViewHolder<*>
        if (type == ListCatalogCardViewHolder.LAYOUT) {
            viewHolder = ListCatalogCardViewHolder(view,catalogCardListener)

        } else if (type == BigGridCatalogCardViewHolder.LAYOUT) {
            viewHolder = BigGridCatalogCardViewHolder(view,catalogCardListener)

        } else if (type == SmallGridCatalogCardViewHolder.LAYOUT) {
            viewHolder = SmallGridCatalogCardViewHolder(view,catalogCardListener)

        } else {
            viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder

    }

}
