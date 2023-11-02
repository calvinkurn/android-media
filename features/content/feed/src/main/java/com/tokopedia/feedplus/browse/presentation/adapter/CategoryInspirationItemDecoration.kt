package com.tokopedia.feedplus.browse.presentation.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.producttag.view.adapter.viewholder.LoadingViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.CategoryInspirationViewHolder
import com.tokopedia.feedplus.presentation.util.findViewHolderByPositionInfo
import com.tokopedia.feedplus.presentation.util.getChildValidPositionInfo
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.feedplus.R

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
        when (parent.findContainingViewHolder(view)) {
            is ChipsViewHolder -> {
                outRect.itemOffsetsChips(parent, view, state)
            }
            is CategoryInspirationViewHolder.Card,
            is CategoryInspirationViewHolder.Placeholder -> {
                outRect.itemOffsetsInspirationCard(parent, view, state)
            }
            is LoadingViewHolder -> {
                outRect.itemOffsetsLoading()
            }
            else -> {
                outRect.itemOffsetsElse()
            }
        }

        outRect.itemOffsetsBottom(parent, view, state)
    }

    private fun Rect.itemOffsetsChips(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val positionInfo = parent.getChildValidPositionInfo(child, state)
        val currPosition = positionInfo.position

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = when (parent.findViewHolderByPositionInfo(positionInfo)) {
            is FeedBrowseTitleViewHolder -> offset12
            else -> offset8
        }
    }

    private fun Rect.itemOffsetsInspirationCard(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(child)
        val lParams = viewHolder.itemView.layoutParams as? GridLayoutManager.LayoutParams ?: return
        val spanIndex = lParams.spanIndex

        val positionInfo = parent.getChildValidPositionInfo(child, state)
        val currPosition = positionInfo.position

        left = if (spanIndex == 0) offset16 else offset4
        right = if (spanIndex == spanCount - 1) offset16 else offset4

        val prevSpanRowPosition = currPosition - spanIndex - 1
        if (prevSpanRowPosition < 0) return

        top = when (parent.findViewHolderByPositionInfo(positionInfo)) {
            is CategoryInspirationViewHolder.Card,
            is CategoryInspirationViewHolder.Placeholder -> {
                offset24
            }
            else -> {
                offset16
            }
        }
    }

    private fun Rect.itemOffsetsLoading() {
        top = offset12
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
