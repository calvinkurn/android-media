package com.tokopedia.tokopedianow.searchcategory.presentation.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.tokopedianow.R
import kotlin.math.cos
import kotlin.math.roundToInt

class ProductItemDecoration(
        private val spacing: Int,
): RecyclerView.ItemDecoration() {

    companion object {
        const val ITEM_POSITION_DEFAULT = -1
        const val SPAN_COUNT_DEFAULT = 1
        const val FIRST_IN_ROW = 0
        const val HORIZONTAL_OFFSET_DEFAULT = 0
        const val VERTICAL_OFFSET_DEFAULT = 0
        const val COS_DEFAULT = 45.0
        const val ONE = 1
        const val ONE_POINT_FIVE = 1.5
        const val TWO = 2
        const val FOUR = 4
    }

    private var verticalCardViewOffset = 0
    private var horizontalCardViewOffset = 0
    private val allowedViewTypes = listOf(
            R.layout.item_tokopedianow_search_category_product
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
        else ITEM_POSITION_DEFAULT
    }

    private fun getTotalSpanCount(parent: RecyclerView) =
            when (val layoutManager = parent.layoutManager) {
                is StaggeredGridLayoutManager -> layoutManager.spanCount
                else -> SPAN_COUNT_DEFAULT
            }

    private fun getHorizontalCardViewOffset(view: View) =
            when (view) {
                is IProductCardView -> getHorizontalOffsetForIProductCardView(view)
                is CardView -> getHorizontalOffsetForCardView(view)
                else -> HORIZONTAL_OFFSET_DEFAULT
            }

    private fun getHorizontalOffsetForIProductCardView(cardView: IProductCardView): Int {
        val maxElevation = cardView.getCardMaxElevation()
        val radius = cardView.getCardRadius()

        return getHorizontalOffset(maxElevation, radius)
    }

    private fun getHorizontalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation + (ONE - cos(COS_DEFAULT)) * radius).toFloat().roundToInt() / 2
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
                else -> VERTICAL_OFFSET_DEFAULT
            }

    private fun getVerticalOffsetForIProductCardView(cardView: IProductCardView): Int {
        val maxElevation = cardView.getCardMaxElevation()
        val radius = cardView.getCardRadius()

        return getVerticalOffset(maxElevation, radius)
    }

    private fun getVerticalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation * ONE_POINT_FIVE + (ONE - cos(COS_DEFAULT)) * radius).toFloat().roundToInt() / TWO
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

    private fun getLeftOffsetNotFirstInRow() = spacing / FOUR - horizontalCardViewOffset

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount))
            getTopOffsetTopItem()
        else
            getTopOffsetNotTopItem()
    }

    private fun getTopOffsetTopItem() = spacing / TWO - verticalCardViewOffset

    private fun getTopOffsetNotTopItem() = spacing / TWO - verticalCardViewOffset

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun getRightOffsetLastInRow() = spacing - horizontalCardViewOffset

    private fun getRightOffsetNotLastInRow() = spacing / FOUR - horizontalCardViewOffset

    private fun getBottomOffsetNotBottomItem() = spacing / FOUR - verticalCardViewOffset

    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - ONE)
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == FIRST_IN_ROW
    }

    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == spanCount - ONE
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != ITEM_POSITION_DEFAULT && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return ITEM_POSITION_DEFAULT
        val isInvalidPosition = viewPosition < FIRST_IN_ROW || viewPosition > adapter.itemCount - ONE

        return if (isInvalidPosition) ITEM_POSITION_DEFAULT else adapter.getItemViewType(viewPosition)
    }
}