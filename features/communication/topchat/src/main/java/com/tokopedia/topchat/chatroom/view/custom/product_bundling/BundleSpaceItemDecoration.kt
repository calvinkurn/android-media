package com.tokopedia.topchat.chatroom.view.custom.product_bundling

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.toPx

open class BundleSpaceItemDecoration(
    private val space: Int = SPACE
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val totalItem = state.itemCount
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        handlePadding(outRect, position, totalItem)
    }

    private fun handlePadding(outRect: Rect, position: Int, totalItem: Int) {
        when {
            (position == 0) -> setupLeftItem(outRect)
            (position == totalItem - 1) -> setupRightItem(outRect)
            else -> setupItem(outRect)
        }
    }

    protected open fun setupLeftItem(outRect: Rect) {
        outRect.set(0, 0, space.toPx(), 0)
    }

    protected open fun setupRightItem(outRect: Rect) {
        outRect.set(space.toPx(), 0, 0, 0)
    }

    protected open fun setupItem(outRect: Rect) {
        outRect.set(space.toPx(), 0, space.toPx(), 0)
    }

    companion object {
        private const val SPACE = 6
    }
}