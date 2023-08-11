package com.tokopedia.search.result.product.grid

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import javax.inject.Inject

class SearchProductGridSpanSizeLookup @Inject constructor(
    private val recyclerViewUpdater: RecyclerViewUpdater,
    private val gridLayoutManager: GridLayoutManager,
) : SpanSizeLookup() {
    private fun isGridFullSpan(viewType: Int): Boolean {
        return viewType != SmallGridProductItemViewHolder.LAYOUT
            && viewType != SmallGridProductItemViewHolder.LAYOUT_WITH_VIEW_STUB
            && viewType != RecommendationItemViewHolder.LAYOUT
            && viewType != SmallGridInspirationCardViewHolder.LAYOUT
    }

    override fun getSpanSize(position: Int): Int {
        val recyclerView = recyclerViewUpdater.recyclerView ?: return 1
        val adapter = recyclerView.adapter ?: return 1
        val viewType = adapter.getItemViewType(position)
        return if (isGridFullSpan(viewType)) gridLayoutManager.spanCount else 1
    }
}
