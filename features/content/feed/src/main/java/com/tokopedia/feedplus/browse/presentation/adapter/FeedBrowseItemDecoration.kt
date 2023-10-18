package com.tokopedia.feedplus.browse.presentation.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseBannerViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseHorizontalChannelsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseInspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseTitleViewHolder
import com.tokopedia.feedplus.presentation.util.findViewHolderByPositionInfo
import com.tokopedia.feedplus.presentation.util.getChildValidPositionInfo

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
    private val offset24 = resources.getDimensionPixelOffset(
        unifyprinciplesR.dimen.spacing_lvl5
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val positionInfo = parent.getChildValidPositionInfo(view, state)
        when (parent.findViewHolderByPositionInfo(positionInfo)) {
            is FeedBrowseTitleViewHolder -> outRect.itemOffsetsTitle()
            is FeedBrowseBannerViewHolder -> outRect.itemOffsetsBanner(parent, view, state)
            is FeedBrowseChipsViewHolder -> outRect.itemOffsetsChips(parent, view, state)
            is FeedBrowseHorizontalChannelsViewHolder -> outRect.itemOffsetsHorizontalChannels()
            is FeedBrowseInspirationCardViewHolder -> outRect.itemOffsetsInspirationCard(parent, view, state)
            else -> outRect.itemOffsetsElse()
        }

        if (positionInfo.position == state.itemCount - 1) {
            outRect.bottom = offset16
        }
    }

    private fun Rect.itemOffsetsTitle() {
        left = offset16
        right = offset16
        top = offset16
    }

    private fun Rect.itemOffsetsChips(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State,
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

    private fun Rect.itemOffsetsBanner(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State,
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
            is FeedBrowseBannerViewHolder -> offset8
            else -> offset12
        }
    }

    private fun Rect.itemOffsetsInspirationCard(
        parent: RecyclerView,
        child: View,
        state: RecyclerView.State,
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
            is FeedBrowseInspirationCardViewHolder -> offset24
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
}
