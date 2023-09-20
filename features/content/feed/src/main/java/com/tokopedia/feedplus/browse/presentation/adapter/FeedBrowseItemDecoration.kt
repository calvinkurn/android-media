package com.tokopedia.feedplus.browse.presentation.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder

/**
 * Created by kenny.hadisaputra on 19/09/23
 */
class FeedBrowseItemDecoration(
    resources: Resources,
    private val spanCount: Int,
) : RecyclerView.ItemDecoration() {

    private val offset4 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl2
    )
    private val offset8 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl3
    )
    private val offset12 = resources.getDimensionPixelOffset(
        R.dimen.feed_space_12
    )
    private val offset16 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl4
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (val viewHolder = parent.getChildViewHolder(view)) {
            is FeedBrowseTitleViewHolder -> outRect.itemOffsetsTitle()
            is FeedBrowseBannerViewHolder -> outRect.itemOffsetsBanner(viewHolder, parent, view)
            is FeedBrowseChannelViewHolder -> outRect.itemOffsetsChannel()
        }

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = offset16
        }
    }

    private fun Rect.itemOffsetsTitle() {
        left = offset16
        right = offset16
        top = offset16
    }

    private fun Rect.itemOffsetsBanner(
        viewHolder: FeedBrowseBannerViewHolder,
        parent: RecyclerView,
        child: View,
    ) {
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val currPosition = parent.getChildAdapterPosition(child)

        left = if (spanIndex == 0) offset16 else offset4
        right = if (spanIndex == spanCount - 1) offset16 else offset4

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = when (parent.findViewHolderForAdapterPosition(prevSpanRowPosition)) {
            is FeedBrowseBannerViewHolder -> offset8
            else -> offset12
        }
    }

    private fun Rect.itemOffsetsChannel() {
        top = offset16
    }
}
