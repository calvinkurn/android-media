package com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationItemGridViewHolder

class HomeFeedItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

        if (isTopProductItem(position)) {
            outRect.top = spacing * 2
        }

        if (!isProductItem(parent, position)) {
            return
        }

        outRect.left = spacing
        outRect.right = spacing

        outRect.bottom = spacing * 2
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val adapter = parent.adapter
        return if (viewPosition < 0 || viewPosition > adapter!!.itemCount - 1) {
            false
        } else adapter.getItemViewType(viewPosition) == HomeRecommendationItemGridViewHolder.LAYOUT
    }

    private fun isTopProductItem(viewPosition: Int): Boolean {
        return viewPosition <= 1
    }
}
