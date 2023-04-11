package com.tokopedia.search.result.product.broadmatch

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardLifecycleObserver

interface BroadMatchListener {
    fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView)

    val productCardLifecycleObserver: ProductCardLifecycleObserver?
}
