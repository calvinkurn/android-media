package com.tokopedia.discovery.categoryrevamp.data.typefactory.product

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.BigGridProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.ListProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.SmallGridProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactoryImpl
import com.tokopedia.discovery.categoryrevamp.view.interfaces.ProductCardListener

class ProductTypeFactoryImpl(var productCardListener: ProductCardListener) : BaseProductTypeFactoryImpl(), ProductTypeFactory {


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
        if (type == ListProductCardViewHolder.LAYOUT) {
            viewHolder = ListProductCardViewHolder(view, productCardListener)

        } else if (type == BigGridProductCardViewHolder.LAYOUT) {
            viewHolder = BigGridProductCardViewHolder(view, productCardListener)

        } else if (type == SmallGridProductCardViewHolder.LAYOUT) {
            viewHolder = SmallGridProductCardViewHolder(view, productCardListener)

        } else {
            viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder

    }
}