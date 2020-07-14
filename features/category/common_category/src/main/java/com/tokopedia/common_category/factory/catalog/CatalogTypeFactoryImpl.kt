package com.tokopedia.common_category.factory.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants

import com.tokopedia.common_category.factory.BaseProductTypeFactoryImpl
import com.tokopedia.common_category.data.catalogModel.CatalogItem
import com.tokopedia.common_category.interfaces.CatalogCardListener
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.BigListcatalogShimmerViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.GridListCatalogShimmerViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.ListCatalogShimmerViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalog.BigGridCatalogCardViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalog.ListCatalogCardViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalog.SmallGridCatalogCardViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.common_category.viewholders.viewholder.catalogShimmer.model.ListCatalogShimmerModel

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
