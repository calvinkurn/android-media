package com.tokopedia.product.detail.view.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.view.viewholder.ProductRecommendationVerticalViewHolder

class RecommendationItemDecoration : RecyclerView.ItemDecoration() {

    private val marginHorizontal = 16f.toPx().toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(view)
        val spanIndex = (view.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.spanIndex
        if (viewHolder is ProductRecommendationVerticalViewHolder && spanIndex != null) {
            when (spanIndex) {
                0 -> {
                    outRect.left = marginHorizontal
                }
                1 -> {
                    outRect.right = marginHorizontal
                }
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }
}
