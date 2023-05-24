package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R

class ComparisonBpcWidgetDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val lastItem = ((parent.layoutManager?.itemCount?:0)-1)
        val currentPosition = parent.layoutManager?.getPosition(view)

        val comparisonWidgetMarginStart = view.resources.getDimensionPixelSize(R.dimen.comparison_bpc_widget_margin_start)
        val comparisonWidgetMarginEnd = view.resources.getDimensionPixelSize(R.dimen.comparison_bpc_widget_margin_end)
        val comparisonWidgetSpacing = view.resources.getDimensionPixelSize(R.dimen.comparison_bpc_widget_spacing)
        when (currentPosition) {
            0 -> {
                outRect.left = comparisonWidgetMarginStart
                outRect.right = comparisonWidgetSpacing/2
            }
            lastItem -> {
                outRect.left = comparisonWidgetSpacing/2
                outRect.right = comparisonWidgetMarginEnd
            }
            else -> {
                outRect.left = comparisonWidgetSpacing/2
                outRect.right = comparisonWidgetSpacing/2
            }
        }
    }
}
