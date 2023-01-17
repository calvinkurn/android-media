package com.tokopedia.tokopedianow.seeallcategories.persentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.R

class SeeAllCategoriesDecoration(
    private val spacing: Int,
): RecyclerView.ItemDecoration() {

    companion object {
        const val INVALID_ITEM_POSITION = -1
        const val INVALID_VIEW_TYPE = -1
        const val ADAPTER_START_INDEX = 0
        const val START_PRODUCT_POSITION = 0
        const val MIDDLE_PRODUCT_POSITION = 1
        const val END_PRODUCT_POSITION = 2
        const val DEFAULT_OFFSET = 0
        const val DIVIDED_BY_TWO = 2
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val absolutePos = parent.getChildAdapterPosition(view)
        val halfSpace = spacing / DIVIDED_BY_TWO

        if (isSeeAllCategoriesItem(parent, absolutePos)) {
            val spanIndex = getSpanIndex(view)

            outRect.left = getLeftProductOffset(spanIndex, halfSpace)
            outRect.top = getTopOffset(halfSpace)
            outRect.right = getRightProductOffset(spanIndex, halfSpace)
            outRect.bottom = halfSpace
        }
    }

    private fun getSpanIndex(view: View): Int {
        val layoutParams = view.layoutParams
        return if (layoutParams is GridLayoutManager.LayoutParams) layoutParams.spanIndex else INVALID_ITEM_POSITION
    }

    private fun getTopOffset(halfSpace: Int): Int = halfSpace

    private fun getLeftProductOffset(relativePos: Int, halfSpace: Int): Int = when (relativePos) {
        START_PRODUCT_POSITION -> spacing
        MIDDLE_PRODUCT_POSITION -> halfSpace
        else -> DEFAULT_OFFSET
    }

    private fun getRightProductOffset(relativePos: Int, halfSpace: Int): Int {
        return when (relativePos) {
            END_PRODUCT_POSITION -> spacing
            MIDDLE_PRODUCT_POSITION -> halfSpace
            else -> DEFAULT_OFFSET
        }
    }

    private fun isSeeAllCategoriesItem(parent: RecyclerView, viewPosition: Int): Boolean = R.layout.item_tokopedianow_see_all_categories == getRecyclerViewViewType(parent, viewPosition)

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_VIEW_TYPE
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount
        return if (isInvalidPosition) INVALID_VIEW_TYPE else adapter.getItemViewType(viewPosition)
    }
}
