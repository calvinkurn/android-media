package com.tokopedia.search.result.presentation.view.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView

interface BroadMatchListener {
    fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView)

    val carouselRecycledViewPool: RecyclerView.RecycledViewPool?

    val productCardLifecycleObserver: ProductCardLifecycleObserver?
}