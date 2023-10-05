package com.tokopedia.shop.common.extension

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toPx

private const val PADDING_BOTTOM_IN_DP = 16

fun RecyclerView.applyPaddingToLastItem(paddingBottomInDp: Int = PADDING_BOTTOM_IN_DP) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildLayoutPosition(view)
            val totalItemCount = state.itemCount
            if (totalItemCount > Int.ZERO && position == totalItemCount - Int.ONE) {
                outRect.bottom = paddingBottomInDp.toPx()
            }
        }
    })
}
