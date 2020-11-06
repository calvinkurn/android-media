package com.tokopedia.topads.common.data.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.R

class SpaceItemDecoration(private val orientation: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: 0) - 1) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                outRect.right = view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
            } else {
                outRect.bottom = view.context.resources.getDimensionPixelSize(R.dimen.dp_8)
            }
        }
    }
}
