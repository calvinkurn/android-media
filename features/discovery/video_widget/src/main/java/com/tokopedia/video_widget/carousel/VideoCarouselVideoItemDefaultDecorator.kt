package com.tokopedia.video_widget.carousel

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class VideoCarouselVideoItemDefaultDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount
        val dp16 = 16.toPx()
        val dp8 = 8.toPx()

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = dp16
            outRect.right = dp8
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = dp16
        } else {
            outRect.right = dp8
        }
    }
}