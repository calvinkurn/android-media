package com.tokopedia.tokomart.searchcategory.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.tokomart.R
import kotlin.math.cos
import kotlin.math.roundToInt

class ProductItemDecoration(
        private val spacing: Int,
): RecyclerView.ItemDecoration() {

    private var verticalCardViewOffset = 0
    private var horizontalCardViewOffset = 0
    private val allowedViewTypes = listOf(
            R.layout.item_tokomart_search_category_product
    )

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val absolutePos = parent.getChildAdapterPosition(view)

        if (isProductItem(parent, absolutePos)) {
            val relativePos = getProductItemRelativePosition(view)
            val totalSpanCount = getTotalSpanCount(parent)

            verticalCardViewOffset = getVerticalCardViewOffset(view)
            horizontalCardViewOffset = getHorizontalCardViewOffset(view)

            outRect.left = getLeftOffset(relativePos, totalSpanCount)
            outRect.top = getTopOffset(parent, absolutePos, relativePos, totalSpanCount)
            outRect.right = getRightOffset(relativePos, totalSpanCount)
            outRect.bottom = getBottomOffsetNotBottomItem()
        }
    }

    private fun getProductItemRelativePosition(view: View): Int {
        val layoutParams = view.layoutParams
        return if (layoutParams is StaggeredGridLayoutManager.LayoutParams)
            layoutParams.spanIndex
        else -1
    }

    private fun getTotalSpanCount(parent: RecyclerView) =
            when (val layoutManager = parent.layoutManager) {
                is StaggeredGridLayoutManager -> layoutManager.spanCount
                else -> 1
            }

    private fun getHorizontalCardViewOffset(view: View) =
            when (view) {
                is IProductCardView -> getHorizontalOffsetForIProductCardView(view)
                is CardView -> getHorizontalOffsetForCardView(view)
                else -> 0
            }

    private fun getHorizontalOffsetForIProductCardView(cardView: IProductCardView): Int {
        val maxElevation = cardView.getCardMaxElevation()
        val radius = cardView.getCardRadius()

        return getHorizontalOffset(maxElevation, radius)
    }

    private fun getHorizontalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
    }

    private fun getHorizontalOffsetForCardView(cardView: CardView): Int {
        val maxElevation = cardView.maxCardElevation
        val radius = cardView.radius

        return getHorizontalOffset(maxElevation, radius)
    }

    private fun getVerticalCardViewOffset(view: View) =
            when (view) {
                is IProductCardView -> getVerticalOffsetForIProductCardView(view)
                is CardView -> getVerticalOffsetForCardView(view)
                else -> 0
            }

    private fun getVerticalOffsetForIProductCardView(cardView: IProductCardView): Int {
        val maxElevation = cardView.getCardMaxElevation()
        val radius = cardView.getCardRadius()

        return getVerticalOffset(maxElevation, radius)
    }

    private fun getVerticalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation * 1.5 + (1 - cos(45.0)) * radius).toFloat().roundToInt() / 2
    }

    private fun getVerticalOffsetForCardView(cardView: CardView): Int {
        val maxElevation = cardView.maxCardElevation
        val radius = cardView.radius

        return getVerticalOffset(maxElevation, radius)
    }

    private fun getLeftOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isFirstInRow(relativePos, totalSpanCount)) getLeftOffsetFirstInRow() else getLeftOffsetNotFirstInRow()
    }

    private fun getLeftOffsetFirstInRow() = spacing - horizontalCardViewOffset

    private fun getLeftOffsetNotFirstInRow() = spacing / 4 - horizontalCardViewOffset

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount))
            getTopOffsetTopItem()
        else
            getTopOffsetNotTopItem()
    }

    private fun getTopOffsetTopItem() = spacing / 2 - verticalCardViewOffset

    private fun getTopOffsetNotTopItem() = spacing / 4 - verticalCardViewOffset

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun getRightOffsetLastInRow() = spacing - horizontalCardViewOffset

    private fun getRightOffsetNotLastInRow() = spacing / 4 - horizontalCardViewOffset

    private fun getBottomOffsetNotBottomItem() = spacing / 4 - verticalCardViewOffset

    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == 0
    }

    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == spanCount - 1
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != -1 && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return -1
        val isInvalidPosition = viewPosition < 0 || viewPosition > adapter.itemCount - 1

        return if (isInvalidPosition) -1 else adapter.getItemViewType(viewPosition)
    }
}