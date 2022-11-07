package com.tokopedia.tokopedianow.recipebookmark.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSpaceItemDecoration(
    private val left: Int = DEFAULT_SPACE,
    private val right: Int = DEFAULT_SPACE,
    private val top: Int = DEFAULT_SPACE,
    private val bottom: Int = DEFAULT_SPACE
): RecyclerView.ItemDecoration() {

    companion object {
        const val DEFAULT_SPACE = 0
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = left
        outRect.right = right
        outRect.bottom = bottom
        outRect.top = top
    }

}