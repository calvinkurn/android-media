package com.tokopedia.tokopedianow.home.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.common.decoration.ProductCardGridDecoration

class ClaimCouponWidgetItemDecoration(
    private val space: Int = DEFAULT_SPACE
): RecyclerView.ItemDecoration() {

    companion object {
        const val DEFAULT_SPACE = 0
        const val INVALID_ITEM_POSITION = -1
        const val START_PRODUCT_POSITION = 0
        const val END_PRODUCT_POSITION = 1
        const val SINGLE_SPAN_COUNT = 1
        const val DOUBLE_SPAN_COUNT = 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanCount = getSpanCount(parent)
        if (spanCount == DOUBLE_SPAN_COUNT) {
            val spanIndex = getSpanIndex(view)
            outRect.left = getLeftProductOffset(spanIndex, space)
            outRect.right = getRightProductOffset(spanIndex, space)
        }
    }

    private fun getSpanIndex(view: View): Int {
        val layoutParams = view.layoutParams
        return if (layoutParams is GridLayoutManager.LayoutParams) layoutParams.spanIndex else INVALID_ITEM_POSITION
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) layoutManager.spanCount else ProductCardGridDecoration.SPAN_COUNT_DEFAULT
    }

    private fun getLeftProductOffset(
        relativePos: Int,
        space: Int
    ): Int = when (relativePos) {
        END_PRODUCT_POSITION -> space
        else -> DEFAULT_SPACE
    }

    private fun getRightProductOffset(
        relativePos: Int,
        space: Int
    ): Int = when (relativePos) {
        START_PRODUCT_POSITION -> space
        else -> DEFAULT_SPACE
    }

}
