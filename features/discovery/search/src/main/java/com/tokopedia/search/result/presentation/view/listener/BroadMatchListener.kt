package com.tokopedia.search.result.presentation.view.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView
import com.tokopedia.search.result.presentation.model.BroadMatchDataView

interface BroadMatchListener {
    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchSeeMoreClicked(broadMatchDataView: BroadMatchDataView)

    fun onBroadMatchThreeDotsClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)

    val carouselRecycledViewPool: RecyclerView.RecycledViewPool?
}