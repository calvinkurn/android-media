package com.tokopedia.reviewseller.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class PaddingItemDecoratingReviewSeller : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) {
            parent.adapter.also {
                outRect.left = 8.toPx()
            }
        }
    }
}