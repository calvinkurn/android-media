package com.tokopedia.recommendation_widget_common.infinite.main

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.recommendation_widget_common.infinite.component.product.InfiniteProductViewHolder

class InfiniteRecommendationItemDecoration : RecyclerView.ItemDecoration() {

    private val marginHorizontal = 12f.toPx().toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(view)
        val spanIndex = (view.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.spanIndex
        if (viewHolder is InfiniteProductViewHolder && spanIndex != null) {
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
