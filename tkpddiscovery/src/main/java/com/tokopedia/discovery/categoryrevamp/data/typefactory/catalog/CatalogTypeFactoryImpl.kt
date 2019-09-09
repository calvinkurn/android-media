package com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.BigListcatalogShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.GridListCatalogShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.ListCatalogShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.ListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.BigGridCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.ListCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.catalog.SmallGridCatalogCardViewHolder
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.CatalogItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.interfaces.CatalogCardListener

class CatalogTypeFactoryImpl(val catalogCardListener: CatalogCardListener) : BaseProductTypeFactoryImpl(), CatalogTypeFactory {
    override fun type(gridListShimmerModel: GridListCatalogShimmerModel): Int {
        return GridListCatalogShimmerViewHolder.LAYOUT
    }

    override fun type(listShimmerModel: ListCatalogShimmerModel): Int {
        return ListCatalogShimmerViewHolder.LAYOUT
    }


    override fun type(bigListShimmerModel: BigListCatalogShimmerModel): Int {
        return BigListcatalogShimmerViewHolder.LAYOUT
    }

    override fun type(catalogItem: CatalogItem): Int {
        return when (getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> ListCatalogCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_1 -> BigGridCatalogCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_2 -> SmallGridCatalogCardViewHolder.LAYOUT
            else -> SmallGridCatalogCardViewHolder.LAYOUT
        }
    }


    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {

        val viewHolder: AbstractViewHolder<*>
        when (type) {
            ListCatalogCardViewHolder.LAYOUT -> viewHolder = ListCatalogCardViewHolder(view, catalogCardListener)
            BigGridCatalogCardViewHolder.LAYOUT -> viewHolder = BigGridCatalogCardViewHolder(view, catalogCardListener)
            SmallGridCatalogCardViewHolder.LAYOUT -> viewHolder = SmallGridCatalogCardViewHolder(view, catalogCardListener)
            GridListCatalogShimmerViewHolder.LAYOUT -> viewHolder = GridListCatalogShimmerViewHolder(view)
            ListCatalogShimmerViewHolder.LAYOUT -> viewHolder = ListCatalogShimmerViewHolder(view)
            BigListcatalogShimmerViewHolder.LAYOUT -> viewHolder = BigListcatalogShimmerViewHolder(view)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder

    }

}
