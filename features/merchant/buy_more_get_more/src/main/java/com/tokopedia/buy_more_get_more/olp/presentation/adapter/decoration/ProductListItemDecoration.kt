package com.tokopedia.buy_more_get_more.olp.presentation.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.setMargin

class ProductListItemDecoration : RecyclerView.ItemDecoration() {

    companion object {

        private const val SPACING_32 = 32
        private const val SPACING_8 = 8
        private const val PRODUCT_LIST_ITEM_POSITION = 2
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position >= PRODUCT_LIST_ITEM_POSITION) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            if (spanIndex.isZero()) { // 0 is left span, and 1 is right span
                view.setPadding(SPACING_32, SPACING_8, SPACING_8, Int.ZERO) //set left padding
                view.setMargin(Int.ZERO,Int.ZERO,Int.ZERO,Int.ZERO) //margin set  to zero to remove view cut off
            } else {
                view.setPadding(SPACING_8, SPACING_8, SPACING_32, Int.ZERO) //set right padding
                view.setMargin(Int.ZERO,Int.ZERO,Int.ZERO,Int.ZERO) //margin set  to zero to remove view cut off
            }
        }
    }
}
