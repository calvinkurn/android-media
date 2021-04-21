package com.tokopedia.recommendation_widget_common.widget.comparison

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R

class ComparisonWidgetDecoration() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = ((parent.layoutManager?.itemCount?:0)-1)
        val currentPosition = parent.layoutManager?.getPosition(view)

        val comparisonWidgetMarginStart = view.resources.getDimensionPixelSize(R.dimen.comparison_widget_margin_start)
        val comparisonWidgetMarginEnd = view.resources.getDimensionPixelSize(R.dimen.comparison_widget_margin_end)
        if (currentPosition == 0) {
            outRect.left = comparisonWidgetMarginStart
        } else if (currentPosition == itemCount) {
            outRect.right = comparisonWidgetMarginEnd
        }
    }
}
