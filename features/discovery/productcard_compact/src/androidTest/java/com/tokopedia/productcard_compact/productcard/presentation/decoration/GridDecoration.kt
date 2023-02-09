package com.tokopedia.productcard_compact.productcard.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.productcard_compact.productcard.utils.ViewUtil.getDpFromDimen

internal class GridDecoration: RecyclerView.ItemDecoration() {

    companion object {
        const val INVALID_ITEM_POSITION = -1
        const val START_PRODUCT_POSITION = 0
        const val MIDDLE_PRODUCT_POSITION = 1
        const val END_PRODUCT_POSITION = 2
        const val DEFAULT_OFFSET = 0
        const val DIVIDED_BY_TWO = 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val space = getDpFromDimen(parent.context, com.tokopedia.unifyprinciples.R.dimen.unify_space_16).toIntSafely()
        val halfSpace = space / DIVIDED_BY_TWO
        val spanIndex = getSpanIndex(view)

        outRect.left = getLeftProductOffset(space, spanIndex, halfSpace)
        outRect.top = getTopOffset(halfSpace)
        outRect.right = getRightProductOffset(space, spanIndex, halfSpace)
        outRect.bottom = halfSpace
    }

    private fun getSpanIndex(view: View): Int {
        val layoutParams = view.layoutParams
        return if (layoutParams is GridLayoutManager.LayoutParams) layoutParams.spanIndex else INVALID_ITEM_POSITION
    }

    private fun getTopOffset(halfSpace: Int): Int = halfSpace

    private fun getLeftProductOffset(space: Int, relativePos: Int, halfSpace: Int): Int = when (relativePos) {
        START_PRODUCT_POSITION -> space
        MIDDLE_PRODUCT_POSITION -> halfSpace
        else -> DEFAULT_OFFSET
    }

    private fun getRightProductOffset(space: Int, relativePos: Int, halfSpace: Int): Int {
        return when (relativePos) {
            END_PRODUCT_POSITION -> space
            MIDDLE_PRODUCT_POSITION -> halfSpace
            else -> DEFAULT_OFFSET
        }
    }
}
