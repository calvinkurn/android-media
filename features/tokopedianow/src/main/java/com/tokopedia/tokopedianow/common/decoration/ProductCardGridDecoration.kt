package com.tokopedia.tokopedianow.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.R

class ProductCardGridDecoration(
    private val spacing: Int,
): RecyclerView.ItemDecoration() {

    companion object {
        const val INVALID_ITEM_POSITION = -1
        const val INVALID_VIEW_TYPE = -1
        const val SPAN_COUNT_DEFAULT = 1
        const val ADAPTER_START_INDEX = 0
        const val START_PRODUCT_POSITION = 0
        const val MIDDLE_PRODUCT_POSITION = 1
        const val END_PRODUCT_POSITION = 2
        const val DEFAULT_OFFSET = 0
        const val DIVIDED_BY_TWO = 2
        const val DIVIDED_BY_FOUR = 4
    }

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val absolutePos = parent.getChildAdapterPosition(view)
        val halfSpace = spacing / DIVIDED_BY_TWO
        val quarterSpace = spacing / DIVIDED_BY_FOUR

        if (isProductItem(parent, absolutePos)) {
            val spanIndex = getSpanIndex(view)
            val spanCount = getSpanCount(parent)

            outRect.left = getLeftProductOffset(spanIndex, halfSpace)
            outRect.top = getTopOffset(parent, absolutePos, spanIndex, spanCount)
            outRect.right = getRightProductOffset(spanIndex, halfSpace)
            outRect.bottom = quarterSpace
        } else if (isChooseAddressWidget(parent, absolutePos)) {
            outRect.top = halfSpace
        } else if (isProductRecommendation(parent, absolutePos)) {
            outRect.bottom = halfSpace
        }
    }

    private fun getSpanIndex(view: View): Int {
        val layoutParams = view.layoutParams
        return if (layoutParams is GridLayoutManager.LayoutParams) layoutParams.spanIndex else INVALID_ITEM_POSITION
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if (layoutManager is GridLayoutManager) layoutManager.spanCount else SPAN_COUNT_DEFAULT
    }

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int = if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount)) spacing / DIVIDED_BY_TWO else spacing / DIVIDED_BY_FOUR

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

    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean = !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1)

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean = R.layout.item_tokopedianow_product_grid_card == getRecyclerViewViewType(parent, viewPosition)

    private fun isChooseAddressWidget(parent: RecyclerView, viewPosition: Int): Boolean = R.layout.item_tokopedianow_choose_address_widget == getRecyclerViewViewType(parent, viewPosition)

    private fun isProductRecommendation(parent: RecyclerView, viewPosition: Int): Boolean = R.layout.item_tokopedianow_product_recommendation == getRecyclerViewViewType(parent, viewPosition)

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_VIEW_TYPE
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount
        return if (isInvalidPosition) INVALID_VIEW_TYPE else adapter.getItemViewType(viewPosition)
    }
}
