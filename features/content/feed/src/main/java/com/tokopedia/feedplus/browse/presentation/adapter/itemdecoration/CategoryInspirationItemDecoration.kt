package com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseItemAdapter
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 30/10/23
 */
internal class CategoryInspirationItemDecoration(
    resources: Resources,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    private val offset4 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl2
    )
    private val offset6 = resources.getDimensionPixelOffset(
        R.dimen.feed_space_6
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
    private val offset24 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl5
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutPosition = parent.getChildLayoutPosition(view)
        if (layoutPosition == RecyclerView.NO_POSITION || layoutPosition >= state.itemCount) return

        val adapter = parent.adapter as? ListAdapter<*, *> ?: return
        try {
            when (adapter.getItemViewType(layoutPosition)) {
                FeedBrowseItemAdapter.TYPE_CHIPS -> {
                    outRect.itemOffsetsChips()
                }
                FeedBrowseItemAdapter.TYPE_INSPIRATION_CARD,
                FeedBrowseItemAdapter.TYPE_INSPIRATION_CARD_PLACEHOLDER -> {
                    outRect.itemOffsetsInspirationCard(parent, view, adapter)
                }
                FeedBrowseItemAdapter.TYPE_LOADING -> {
                    outRect.itemOffsetsLoading()
                }
                FeedBrowseItemAdapter.TYPE_TITLE -> outRect.itemOffsetsTitle()
                else -> {
                    outRect.itemOffsetsElse()
                }
            }

            outRect.itemOffsetsBottom(parent, view, state)
        } catch (_: Exception) { }
    }

    private fun Rect.itemOffsetsChips() {
        top = offset8
    }

    private fun Rect.itemOffsetsInspirationCard(
        parent: RecyclerView,
        child: View,
        adapter: ListAdapter<*, *>
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val currPosition = parent.getChildLayoutPosition(child)

        if (spanCount <= 2) {
            left = if (spanIndex == 0) offset16 else offset6
            right = if (spanIndex == spanCount - 1) offset16 else offset6
        } else {
            left = offset16 * (spanCount - spanIndex) / spanCount
            right = offset16 * (spanIndex + 1) / spanCount
        }

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = offset16
    }

    private fun Rect.itemOffsetsLoading() {
        top = offset12
    }

    private fun Rect.itemOffsetsTitle() {
        left = offset16
        right = offset16
        top = offset24
    }

    private fun Rect.itemOffsetsElse() {
        top = offset12
        left = offset16
    }

    private fun Rect.itemOffsetsBottom(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return

        val currPos = parent.getChildLayoutPosition(child)
        val nextSpanRowPosition = currPos + (spanCount - (lParams.spanIndex + lParams.spanSize)) + 1
        if (nextSpanRowPosition < state.itemCount) return

        bottom = offset16
    }
}
