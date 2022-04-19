package com.tokopedia.tokopedianow.search.presentation.listener

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchDataView
import com.tokopedia.tokopedianow.search.presentation.model.BroadMatchItemDataView

interface BroadMatchListener {

    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool

    fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemClicked(broadMatchItemDataView: BroadMatchItemDataView)

    fun onBroadMatchItemATCNonVariant(
        broadMatchItemDataView: BroadMatchItemDataView,
        quantity: Int,
        broadMatchIndex: Int,
    )

    fun onSaveCarouselScrollPosition(adapterPosition: Int, scrollPosition: Int)

    fun onGetCarouselScrollPosition(adapterPosition: Int): Int

    fun onBroadMatchSeeAllClicked(broadMatchDataView: BroadMatchDataView)
}