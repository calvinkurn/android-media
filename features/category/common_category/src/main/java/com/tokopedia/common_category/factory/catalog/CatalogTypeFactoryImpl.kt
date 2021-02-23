package com.tokopedia.common_category.factory.catalog

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.BaseProductTypeFactoryImpl
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.common_category.interfaces.ProductCardListener
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.shimmer.BigListShimmerModel
import com.tokopedia.common_category.model.shimmer.GridListShimmerModel
import com.tokopedia.common_category.model.shimmer.ListShimmerModel
import com.tokopedia.common_category.viewholders.shimmer.BigListShimmerViewHolder
import com.tokopedia.common_category.viewholders.shimmer.GridListShimmerViewHolder
import com.tokopedia.common_category.viewholders.shimmer.ListShimmerViewHolder
import com.tokopedia.common_category.viewholders.viewholder.catalog.ListCatalogCardViewHolder

class CatalogTypeFactoryImpl(private var productCardListener: ProductCardListener) : BaseProductTypeFactoryImpl(), ProductTypeFactory {

    override fun type(bigListShimmerModel: BigListShimmerModel): Int {
        return BigListShimmerViewHolder.LAYOUT
    }

    override fun type(listShimmerModel: ListShimmerModel): Int {
        return ListShimmerViewHolder.LAYOUT
    }

    override fun type(gridListShimmerModel: GridListShimmerModel): Int {
        return GridListShimmerViewHolder.LAYOUT
    }

    override fun type(productsItem: ProductsItem): Int {
        when (getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> return ListCatalogCardViewHolder.LAYOUT
            else -> return ListShimmerViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            ListShimmerViewHolder.LAYOUT -> viewHolder = ListShimmerViewHolder(view)
            ListCatalogCardViewHolder.LAYOUT -> viewHolder = ListCatalogCardViewHolder(view, productCardListener)
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder

    }
}
