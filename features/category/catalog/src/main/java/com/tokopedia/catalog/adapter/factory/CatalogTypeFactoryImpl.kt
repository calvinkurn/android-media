package com.tokopedia.catalog.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.viewholder.products.*
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.BaseProductTypeFactoryImpl

class CatalogTypeFactoryImpl(private var catalogProductCardListener : CatalogProductCardListener,
                             private val catalogId : String = "",
                             private val categoryId : String = "",
                             private val brand : String = "",
                             private val lifeCycleOwner : FragmentActivity) : BaseProductTypeFactoryImpl(), CatalogTypeFactory {

    override fun type(listShimmerModel: CatalogListShimmerModel): Int {
        return CatalogListShimmerViewHolder.LAYOUT
    }

    override fun type(catalogItem: CatalogProductItem): Int {
        return when (getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> CatalogListProductViewHolder.LAYOUT
            else -> CatalogListShimmerViewHolder.LAYOUT
        }
    }

    override fun type(model: CatalogForYouContainerDataModel): Int {
        return CatalogForYouContainerViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CatalogForYouContainerViewHolder.LAYOUT -> CatalogForYouContainerViewHolder(
                view,
                lifeCycleOwner,
                catalogId,
                categoryId,
                brand,
                catalogProductCardListener
            )
            CatalogListShimmerViewHolder.LAYOUT -> CatalogListShimmerViewHolder(view)
            CatalogListProductViewHolder.LAYOUT -> CatalogListProductViewHolder(
                view,
                catalogProductCardListener
            )
            else -> super.createViewHolder(view, type)
        }
    }
}
