package com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemAdapter
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 19/09/23
 */
class FeedBrowseItemDecoration(
    resources: Resources,
    private val spanCount: Int
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
        val layoutPosition = parent.getChildLayoutPosition(view)
        if (layoutPosition == RecyclerView.NO_POSITION || layoutPosition >= state.itemCount) return

        if (layoutPosition == 0) {
            view.setGapOnFirstItem()
        }

        val adapter = parent.adapter as? ListAdapter<*, *> ?: return
        try {
            when (adapter.getItemViewType(layoutPosition)) {
                FeedBrowseItemAdapter.TYPE_CHIPS -> outRect.itemOffsetsChips(parent, view, adapter)
                FeedBrowseItemAdapter.TYPE_HORIZONTAL_CHANNELS,
                FeedBrowseItemAdapter.TYPE_HORIZONTAL_AUTHORS -> outRect.itemOffsetsHorizontalChannels()
                FeedBrowseItemAdapter.TYPE_BANNER,
                FeedBrowseItemAdapter.TYPE_BANNER_PLACEHOLDER -> outRect.itemOffsetsBanner(parent, view, adapter, state)
                FeedBrowseItemAdapter.TYPE_TITLE -> outRect.itemOffsetsTitle(parent, view)
                FeedBrowseItemAdapter.TYPE_HORIZONTAL_STORIES -> outRect.itemOffsetsHorizontalStories(parent, view)
                else -> outRect.itemOffsetsElse()
            }

            outRect.itemOffsetsBottom(parent, view, state)
        } catch (_: Exception) { }
    }

    private fun Rect.itemOffsetsTitle(
        parent: RecyclerView,
        child: View
    ) {
        left = offset16
        right = offset16
        top = if (parent.getChildLayoutPosition(child) == 0) offset8 else offset16
    }

    private fun Rect.itemOffsetsChips(
        parent: RecyclerView,
        child: View,
        adapter: ListAdapter<*, *>
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val currPosition = parent.getChildLayoutPosition(child)

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = when (adapter.getItemViewType(prevSpanRowPosition)) {
            FeedBrowseItemAdapter.TYPE_TITLE -> offset12
            else -> offset8
        }
    }

    private fun Rect.itemOffsetsBanner(
        parent: RecyclerView,
        child: View,
        adapter: ListAdapter<*, *>,
        state: State
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val currPosition = parent.getChildLayoutPosition(child)

        //https://stackoverflow.com/a/32971704
        if (spanCount <= 2) {
            left = if (spanIndex == 0) offset16 else offset4
            right = if (spanIndex == spanCount - 1) offset16 else offset4
        } else {
            left = offset16 * (spanCount - spanIndex) / spanCount
            right = offset16 * (spanIndex + 1) / spanCount
        }

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = when (adapter.getItemViewType(prevSpanRowPosition)) {
            FeedBrowseItemAdapter.TYPE_BANNER, FeedBrowseItemAdapter.TYPE_BANNER_PLACEHOLDER -> offset8
            else -> offset12
        }

        val nextSpanRowPosition = currPosition + (spanCount - spanIndex - lParams.spanSize) + 1
        if (nextSpanRowPosition >= state.itemCount) return

        bottom = when (adapter.getItemViewType(nextSpanRowPosition)) {
            FeedBrowseItemAdapter.TYPE_BANNER, FeedBrowseItemAdapter.TYPE_BANNER_PLACEHOLDER -> 0
            else -> offset16
        }
    }

    private fun Rect.itemOffsetsElse() {
        top = offset12
        left = offset16
    }

    private fun Rect.itemOffsetsHorizontalChannels() {
        top = offset12
        bottom = offset16
    }

    private fun Rect.itemOffsetsHorizontalStories(
        parent: RecyclerView,
        child: View,
    ) {
        top = if (parent.getChildLayoutPosition(child) == 0) 0 else offset16
    }

    private fun Rect.itemOffsetsBottom(
        parent: RecyclerView,
        child: View,
        state: State
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return

        val currPos = parent.getChildLayoutPosition(child)
        val nextSpanRowPosition = currPos + (spanCount - (lParams.spanIndex + lParams.spanSize)) + 1
        if (nextSpanRowPosition < state.itemCount) return

        bottom = offset16
    }

    private fun View.setGapOnFirstItem() {
        setPadding(paddingLeft, offset16, paddingRight, paddingBottom)
    }
}
