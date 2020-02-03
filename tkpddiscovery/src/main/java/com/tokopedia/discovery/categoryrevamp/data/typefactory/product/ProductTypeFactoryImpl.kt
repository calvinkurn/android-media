package com.tokopedia.discovery.categoryrevamp.data.typefactory.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.HotlistLoadMoreViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.BigGridProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.ListProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.SmallGridProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.BigListShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.GridListShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.ListShimmerViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.BigListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.GridListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.ListShimmerModel
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener

class ProductTypeFactoryImpl(var productCardListener: ProductCardListener) : BaseProductTypeFactoryImpl(), ProductTypeFactory {

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