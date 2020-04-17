package com.tokopedia.common_category.factory.product

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
import com.tokopedia.common_category.viewholders.BigGridProductCardViewHolder
import com.tokopedia.common_category.viewholders.HotlistLoadMoreViewHolder
import com.tokopedia.common_category.viewholders.ListProductCardViewHolder
import com.tokopedia.common_category.viewholders.SmallGridProductCardViewHolder
import com.tokopedia.common_category.viewholders.shimmer.BigListShimmerViewHolder
import com.tokopedia.common_category.viewholders.shimmer.GridListShimmerViewHolder
import com.tokopedia.common_category.viewholders.shimmer.ListShimmerViewHolder


class ProductTypeFactoryImpl(private var productCardListener: ProductCardListener) : BaseProductTypeFactoryImpl(), ProductTypeFactory {

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
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> return ListProductCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_1 -> return BigGridProductCardViewHolder.LAYOUT
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_2 -> return SmallGridProductCardViewHolder.LAYOUT
            else -> return SmallGridProductCardViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            ListProductCardViewHolder.LAYOUT -> viewHolder = ListProductCardViewHolder(view, productCardListener)
            BigGridProductCardViewHolder.LAYOUT -> viewHolder = BigGridProductCardViewHolder(view, productCardListener)
            SmallGridProductCardViewHolder.LAYOUT -> viewHolder = SmallGridProductCardViewHolder(view, productCardListener)
            GridListShimmerViewHolder.LAYOUT -> viewHolder = GridListShimmerViewHolder(view)
            ListShimmerViewHolder.LAYOUT -> viewHolder = ListShimmerViewHolder(view)
            BigListShimmerViewHolder.LAYOUT -> viewHolder = BigListShimmerViewHolder(view)
            HotlistLoadMoreViewHolder.LAYOUT -> viewHolder = HotlistLoadMoreViewHolder(view)
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder

    }
}