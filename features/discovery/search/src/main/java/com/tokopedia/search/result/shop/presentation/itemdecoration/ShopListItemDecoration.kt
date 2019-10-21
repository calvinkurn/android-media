package com.tokopedia.search.result.shop.presentation.itemdecoration

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.search.R

class ShopListItemDecoration(private val left: Int,
                             private val top: Int,
                             private val right: Int,
                             private val bottom: Int) : RecyclerView.ItemDecoration() {

    private val allowedViewTypes = listOf(
            R.layout.search_result_shop_card
    )

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        val absolutePosition = parent.getChildAdapterPosition(view)

        if(isShopItem(parent, absolutePosition)) {
            val relativePosition = getShopItemRelativePosition(parent, view)
            val totalSpanCount = getTotalSpanCount(parent)

            outRect.left = getLeftOffset()
            outRect.top = getTopOffset(parent, absolutePosition, relativePosition, totalSpanCount)
            outRect.right = getRightOffset()
            outRect.bottom = getBottomOffset()
        }
    }

    private fun isShopItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != -1 && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        return parent.adapter?.let { getRecyclerViewViewTypeIfAdapterNotNull(viewPosition, it) } ?: -1
    }

    private fun getRecyclerViewViewTypeIfAdapterNotNull(
            viewPosition: Int,
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ): Int {
        return if (viewPosition < 0 || viewPosition > adapter.itemCount - 1) {
            -1
        } else {
            adapter.getItemViewType(viewPosition)
        }
    }

    private fun getShopItemRelativePosition(parent: RecyclerView, view: View): Int {
        val absolutePos = parent.getChildAdapterPosition(view)
        var firstProductItemPos = absolutePos
        while (isShopItem(parent, firstProductItemPos - 1)) firstProductItemPos--
        return absolutePos - firstProductItemPos
    }

    private fun getTotalSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return if(layoutManager is GridLayoutManager) {
            layoutManager.spanCount
        }
        else {
            1
        }
    }

    private fun getLeftOffset(): Int {
        return left
    }

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopShopItem(parent, absolutePos, relativePos, totalSpanCount)) top else top / 4
    }

    private fun isTopShopItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isShopItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun getRightOffset(): Int {
        return right
    }

    private fun getBottomOffset(): Int {
        return bottom / 4
    }
}