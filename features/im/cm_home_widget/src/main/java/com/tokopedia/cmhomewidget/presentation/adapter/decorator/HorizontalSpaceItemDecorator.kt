package com.tokopedia.cmhomewidget.presentation.adapter.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx


class HorizontalSpaceItemDecorator(
    private val startEndSpacingInDP: Int,
    private val middleSpacingInDP: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val currentItemPosition = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        when (currentItemPosition) {
            0 -> {
                outRect.left = startEndSpacingInDP.toPx()
                outRect.right = middleSpacingInDP.toPx()
            }
            itemCount - 1 -> {
                outRect.left = middleSpacingInDP.toPx()
                outRect.right = startEndSpacingInDP.toPx()
            }
            else -> {
                outRect.left = middleSpacingInDP.toPx()
                outRect.right = middleSpacingInDP.toPx()
            }
        }
    }
}