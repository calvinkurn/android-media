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
                outRect.left = BIG_HORIZONTAL_OFFSET.toPx()
                outRect.right = HORIZONTAL_OFFSET.toPx()
            }
            itemCount - 1 -> {
                outRect.left = HORIZONTAL_OFFSET.toPx()
                outRect.right = BIG_HORIZONTAL_OFFSET.toPx()
            }
            else -> {
                outRect.left = HORIZONTAL_OFFSET.toPx()
                outRect.right = HORIZONTAL_OFFSET.toPx()
            }
        }
        outRect.bottom = BOTTOM_OFFSET.toPx()
    }

    companion object {
        private const val BIG_HORIZONTAL_OFFSET = 16
        private const val BOTTOM_OFFSET = 12
        private const val HORIZONTAL_OFFSET = 4
    }
}
