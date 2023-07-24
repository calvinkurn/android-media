package com.tokopedia.carouselproductcard.paging

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.paging.list.ProductCardListDataView
import com.tokopedia.carouselproductcard.paging.list.ProductCardListViewHolder
import com.tokopedia.carouselproductcard.paging.loading.ShimmeringDataView
import com.tokopedia.carouselproductcard.paging.loading.ShimmeringViewHolder

internal class TypeFactoryImpl(
    private val paddingStart: Int,
): TypeFactory {
    override fun type(productCardListDataView: ProductCardListDataView): Int {
        return ProductCardListViewHolder.LAYOUT
    }

    override fun type(shimmeringDataView: ShimmeringDataView): Int {
        return ShimmeringViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(view: View, viewType: Int): AbstractViewHolder<*> =
        when(viewType) {
            ProductCardListViewHolder.LAYOUT -> ProductCardListViewHolder(view, paddingStart)
            ShimmeringViewHolder.LAYOUT -> ShimmeringViewHolder(view)
            else -> throw Exception("Unknown View Type")
        }
}
