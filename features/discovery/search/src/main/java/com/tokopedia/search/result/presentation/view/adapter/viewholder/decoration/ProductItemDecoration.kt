package com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration

import android.graphics.Rect
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.search.R
import com.tokopedia.search.utils.BASE_RADIUS_AREA
import com.tokopedia.search.utils.CORNER_RADIUS_DEGREE
import com.tokopedia.search.utils.VERTICAL_SHADOW_MULTIPLIER
import java.util.*
import kotlin.math.cos
import kotlin.math.roundToInt

class ProductItemDecoration(private val spacing: Int) : ItemDecoration() {

    private var verticalCardViewOffset = 0
    private var horizontalCardViewOffset = 0
    private val allowedViewTypes = listOf(
            R.layout.search_result_product_card_small_grid,
            R.layout.search_result_product_card_big_grid,
            R.layout.search_result_product_card_list,
            R.layout.search_result_recommendation_card_small_grid,
            R.layout.search_result_product_big_grid_inspiration_card_layout,
            R.layout.search_result_product_small_grid_inspiration_card_layout
    )

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val absolutePos = parent.getChildAdapterPosition(view)

        if (isProductItem(parent, absolutePos)) {
            val relativePos = getProductItemRelativePosition(parent, view)
            val totalSpanCount = getTotalSpanCount(parent)

            verticalCardViewOffset = getVerticalCardViewOffset(view)
            horizontalCardViewOffset = getHorizontalCardViewOffset(view)

            outRect.left = getLeftOffset(relativePos, totalSpanCount)
            outRect.top = getTopOffset(parent, absolutePos, relativePos, totalSpanCount)
            outRect.right = getRightOffset(relativePos, totalSpanCount)
            outRect.bottom = getBottomOffsetNotBottomItem()
        }
    }

    private fun getProductItemRelativePosition(parent: RecyclerView, view: View): Int {
        return if (view.layoutParams is StaggeredGridLayoutManager.LayoutParams)
            getProductItemRelativePositionStaggeredGrid(view)
        else getProductItemRelativePositionNotStaggeredGrid(parent, view)
    }

    private fun getProductItemRelativePositionStaggeredGrid(view: View): Int {
        val staggeredGridLayoutParams = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        return staggeredGridLayoutParams.spanIndex
    }

    private fun getProductItemRelativePositionNotStaggeredGrid(parent: RecyclerView, view: View): Int {
        val absolutePos = parent.getChildAdapterPosition(view)
        var firstProductItemPos = absolutePos
        while (isProductItem(parent, firstProductItemPos - 1)) firstProductItemPos--
        return absolutePos - firstProductItemPos
    }

    private fun getTotalSpanCount(parent: RecyclerView) =
            when (val layoutManager = parent.layoutManager) {
                is GridLayoutManager -> layoutManager.spanCount
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

    /**
     * shadow calculation formula (include the const value) based on CardView shadow formula
     * https://developer.android.com/reference/kotlin/androidx/cardview/widget/CardView
     */
    @Suppress("MagicNumber")
    private fun getHorizontalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation + (BASE_RADIUS_AREA - cos(CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt() / 2
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

    /**
     * shadow calculation formula (include the const value) based on CardView shadow formula
     * https://developer.android.com/reference/kotlin/androidx/cardview/widget/CardView
     */
    @Suppress("MagicNumber")
    private fun getVerticalOffset(maxElevation: Float, radius: Float): Int {
        return (maxElevation * VERTICAL_SHADOW_MULTIPLIER + (BASE_RADIUS_AREA - cos(
            CORNER_RADIUS_DEGREE)) * radius).toFloat().roundToInt() / 2
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

    private fun getLeftOffsetNotFirstInRow() = spacing / LEFT_OFFSET_NOT_FIRST_ITEM_DIVISOR - horizontalCardViewOffset

    private fun getTopOffset(parent: RecyclerView, absolutePos: Int, relativePos: Int, totalSpanCount: Int): Int {
        return if (isTopProductItem(parent, absolutePos, relativePos, totalSpanCount))
            getTopOffsetTopItem()
        else
            getTopOffsetNotTopItem()
    }

    private fun getTopOffsetTopItem() = spacing / 2 - verticalCardViewOffset

    private fun getTopOffsetNotTopItem() = spacing / TOP_OFFSET_NOT_TOP_ITEM_DIVISOR - verticalCardViewOffset

    private fun getRightOffset(relativePos: Int, totalSpanCount: Int): Int {
        return if (isLastInRow(relativePos, totalSpanCount)) getRightOffsetLastInRow() else getRightOffsetNotLastInRow()
    }

    private fun getRightOffsetLastInRow() = spacing - horizontalCardViewOffset

    private fun getRightOffsetNotLastInRow() = spacing / RIGHT_OFFSET_NOT_LAST_ITEM_DIVISOR - horizontalCardViewOffset

    private fun getBottomOffsetNotBottomItem() = spacing / BOTTOM_OFFSET_NOT_BOTTOM_ITEM_DIVISOR - verticalCardViewOffset

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

    companion object{
        private const val RIGHT_OFFSET_NOT_LAST_ITEM_DIVISOR = 4
        private const val TOP_OFFSET_NOT_TOP_ITEM_DIVISOR = 4
        private const val LEFT_OFFSET_NOT_FIRST_ITEM_DIVISOR = 4
        private const val BOTTOM_OFFSET_NOT_BOTTOM_ITEM_DIVISOR = 4
    }
}