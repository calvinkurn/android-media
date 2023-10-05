package com.tokopedia.carouselproductcard.paging

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.paging.list.ProductCardListDataView
import com.tokopedia.carouselproductcard.paging.loading.ShimmeringDataView

internal interface TypeFactory {

    fun type(productCardListDataView: ProductCardListDataView): Int
    fun type(shimmeringDataView: ShimmeringDataView): Int
    fun onCreateViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
