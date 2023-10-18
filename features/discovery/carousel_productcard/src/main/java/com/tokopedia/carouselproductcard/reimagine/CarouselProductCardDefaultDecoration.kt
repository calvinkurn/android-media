package com.tokopedia.carouselproductcard.reimagine

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

internal class CarouselProductCardDefaultDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        val isFirstPosition = parent.getChildAdapterPosition(view) == 0
        val isLastPosition = itemCount > 0 && itemPosition == itemCount - 1

        outRect.left = if (isFirstPosition) 16.toPx() else 6.toPx()
        outRect.right = if (isLastPosition) 16.toPx() else 6.toPx()
    }
}
