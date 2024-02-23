package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.productcard.ProductCardLifecycleObserver

interface BroadMatchListener {
    fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView, adapterPosition: Int)

    fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView, adapterPosition: Int)

    fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView)

    val productCardLifecycleObserver: ProductCardLifecycleObserver?
}
