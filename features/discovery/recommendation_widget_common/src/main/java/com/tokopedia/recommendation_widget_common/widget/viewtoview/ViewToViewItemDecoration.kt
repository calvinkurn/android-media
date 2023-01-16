package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.toPx

class ViewToViewItemDecoration : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount.orZero()

        when (position) {
            0 -> {
                outRect.left = 16.toPx()
                outRect.right = 4.toPx()
            }
            itemCount - 1 -> {
                outRect.left = 4.toPx()
                outRect.right = 16.toPx()
            }
            else -> {
                outRect.left = 4.toPx()
                outRect.right = 4.toPx()
            }
        }
        outRect.bottom = 12.toPx()
    }
}
