package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.recommendation_widget_common.infinite.foryou.entity.ContentCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.play.PlayCardViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardGridViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardListViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.topads.viewholder.BannerTopAdsViewHolder
import com.tokopedia.home.R as homeR

class HomeFeedItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        val isFullSpan = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan

        if (isSingleItem(parent)) {
            val outerPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_outer_padding)
            outRect.left = outerPadding
            outRect.right = outerPadding
            outRect.top = 0
            outRect.bottom = 0
        } else {
            val innerVerticalPadding: Int
            val innerHorizontalPadding: Int
            val outerPadding: Int
            if (hasInternalPadding(parent, position)) {
                innerVerticalPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_inner_vertical_padding_with_internal_padding) / 2
                innerHorizontalPadding = 0
                outerPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_outer_padding_with_internal_padding)
            } else {
                innerVerticalPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_inner_vertical_padding) / 2
                innerHorizontalPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_inner_horizontal_padding) / 2
                outerPadding = view.context.resources.getDimensionPixelSize(homeR.dimen.for_you_outer_padding)
            }

            if (isFullSpan) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = outerPadding
                outRect.right = outerPadding
            } else {
                if (spanIndex == 0) {
                    outRect.left = outerPadding
                    outRect.right = innerHorizontalPadding
                } else if (spanIndex == 1) {
                    outRect.left = innerHorizontalPadding
                    outRect.right = outerPadding
                }

                if (position < 2) {
                    outRect.top = 0
                    outRect.bottom = innerVerticalPadding
                } else {
                    outRect.top = innerVerticalPadding
                    outRect.bottom = innerVerticalPadding
                }
            }
        }
    }

    private fun hasInternalPadding(parent: RecyclerView, viewPosition: Int): Boolean {
        val adapter = parent.adapter
        return if (viewPosition < 0 || viewPosition > (adapter?.itemCount ?: 0) - 1) {
            false
        } else {
            when (adapter?.getItemViewType(viewPosition)) {
                // global
                RecommendationCardGridViewHolder.LAYOUT,
                RecommendationCardListViewHolder.LAYOUT,
                BannerTopAdsViewHolder.LAYOUT,
                ContentCardViewHolder.LAYOUT,
                PlayCardViewHolder.LAYOUT -> true
                else -> false
            }
        }
    }

    private fun isSingleItem(parent: RecyclerView): Boolean {
        return parent.adapter?.itemCount == 1
    }
}
