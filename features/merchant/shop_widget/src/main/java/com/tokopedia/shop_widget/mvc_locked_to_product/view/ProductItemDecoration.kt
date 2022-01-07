package com.tokopedia.shop_widget.mvc_locked_to_product.view

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.shop_widget.R
import kotlin.math.cos
import kotlin.math.roundToInt

class ProductItemDecoration(
        private val spacing: Int,
): RecyclerView.ItemDecoration() {

    companion object {
        const val INVALID_ITEM_POSITION = -1
        const val INVALID_VIEW_TYPE = -1
        const val SPAN_COUNT_DEFAULT = 1
        const val FIRST_IN_ROW_MODULO = 0
        const val HORIZONTAL_OFFSET_DEFAULT = 0
        const val VERTICAL_OFFSET_DEFAULT = 0
        const val ADAPTER_START_INDEX = 0
    }

    private var verticalCardViewOffset = 0
    private var horizontalCardViewOffset = 0
    private val allowedViewTypes = listOf(
            R.layout.item_mvc_locked_to_product_product_grid_card_layout
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
        else INVALID_ITEM_POSITION
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

    @Suppress("MagicNumber")
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
                else -> VERTICAL_OFFSET_DEFAULT
            }

    private fun getVerticalOffsetForIProductCardView(cardView: IProductCardView): Int {
        val maxElevation = cardView.getCardMaxElevation()
        val radius = cardView.getCardRadius()

        return getVerticalOffset(maxElevation, radius)
    }

    @Suppress("MagicNumber")
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

    @Suppress("MagicNumber")
    private fun getLeftOffsetNotFirstInRow() = spacing / 4 - horizontalCardViewOffset

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount))
            getTopOffsetTopItem()
        else
            getTopOffsetNotTopItem()
    }

    @Suppress("MagicNumber")
    private fun getTopOffsetTopItem() = spacing / 2 - verticalCardViewOffset

    @Suppress("MagicNumber")
    private fun getTopOffsetNotTopItem() = spacing / 4 - verticalCardViewOffset

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun getRightOffsetLastInRow() = spacing - horizontalCardViewOffset

    @Suppress("MagicNumber")
    private fun getRightOffsetNotLastInRow() = spacing / 4 - horizontalCardViewOffset

    @Suppress("MagicNumber")
    private fun getBottomOffsetNotBottomItem() = spacing / 4 - verticalCardViewOffset

    @Suppress("MagicNumber")
    private fun isTopProductItem(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Boolean {
        return !isProductItem(parent, absolutePos - relativePos % totalSpanCount - 1)
    }

    private fun isFirstInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == FIRST_IN_ROW_MODULO
    }

    @Suppress("MagicNumber")
    private fun isLastInRow(relativePos: Int, spanCount: Int): Boolean {
        return relativePos % spanCount == spanCount - 1
    }

    private fun isProductItem(parent: RecyclerView, viewPosition: Int): Boolean {
        val viewType = getRecyclerViewViewType(parent, viewPosition)
        return viewType != INVALID_VIEW_TYPE && allowedViewTypes.contains(viewType)
    }

    private fun getRecyclerViewViewType(parent: RecyclerView, viewPosition: Int): Int {
        val adapter = parent.adapter ?: return INVALID_VIEW_TYPE
        val isInvalidPosition = viewPosition !in ADAPTER_START_INDEX until adapter.itemCount

        return if (isInvalidPosition) INVALID_VIEW_TYPE else adapter.getItemViewType(viewPosition)
    }
}