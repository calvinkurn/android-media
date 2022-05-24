package com.tokopedia.topchat.chatroom.view.custom.product_bundling

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

class BundleSpaceItemDecoration(
    private val spaceDp: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val totalItem = parent.itemDecorationCount
        when {
            //First item
            (position == 0) -> {
                outRect.right = spaceDp.toPx() / 2
                outRect.left = spaceDp.toPx()
            }
            //Last item
            (position > 0  && position == totalItem - 1) -> {
                outRect.right = spaceDp.toPx()
                outRect.left = spaceDp.toPx() / 2
            }
            else -> {
                outRect.right = spaceDp.toPx() / 2
                outRect.left = spaceDp.toPx() / 2
            }
        }
    }
}