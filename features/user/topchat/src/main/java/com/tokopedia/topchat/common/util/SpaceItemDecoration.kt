package com.tokopedia.topchat.common.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class SpaceItemDecoration(
    private val spaceDpLeft: Int,
    private val spaceDpRight: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.right = spaceDpLeft.toPx()
                outRect.left = 0
            }
            state.itemCount - 1 -> {
                outRect.right = 0
                outRect.left = spaceDpRight.toPx()
            }
            else -> {
                outRect.right = spaceDpLeft.toPx()
                outRect.left = spaceDpRight.toPx()
            }
        }
    }
}