package com.tokopedia.product_ar.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.dpToPx
import kotlin.math.roundToInt

class ItemDividerGrid()
    : RecyclerView.ItemDecoration() {

    companion object {
        const val MARGIN_PER_GRID = 2F
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        ///horizontal
        if (position > 1) {
            outRect.top = MARGIN_PER_GRID.dpToPx().roundToInt()
        } else {
            outRect.bottom = MARGIN_PER_GRID.dpToPx().roundToInt()
        }

        if (position % 2 == 0) {
            outRect.right = MARGIN_PER_GRID.dpToPx().roundToInt()
        } else {
            outRect.left = MARGIN_PER_GRID.dpToPx().roundToInt()
        }
    }
}