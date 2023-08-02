package com.tokopedia.carouselproductcard.paging

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.paging.list.ProductCardListViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.unifycomponents.toPx

internal class ItemDecoration(
    private val context: Context,
    private val paddingHorizontalPx: Int,
    private val itemPerPage: Int,
): DividerItemDecoration(context, VERTICAL) {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val absolutePosition = parent.getChildAdapterPosition(view)

        outRect.left = getLeftPadding(absolutePosition)
        outRect.right = getRightPadding(parent, absolutePosition)
        outRect.bottom = BOTTOM_PADDING_DP.toPx()
    }

    private fun getLeftPadding(absolutePosition: Int): Int {
        val firstPageDivisor = 1
        val defaultDivisor = 2
        val isFirstPage = absolutePosition < itemPerPage
        val divisor = if (isFirstPage) firstPageDivisor else defaultDivisor

        return paddingHorizontalPx / divisor
    }

    private fun getRightPadding(parent: RecyclerView, absolutePosition: Int): Int {
        val defaultRightPadding = paddingHorizontalPx / 2
        val adapter = parent.adapter ?: return defaultRightPadding
        val itemCount = adapter.itemCount
        val viewType = adapter.getItemViewType(absolutePosition)

        val isLastPage = getIsLastItem(viewType, absolutePosition, itemCount)

        return if (isLastPage) getRemainingScreenSize() - paddingHorizontalPx
        else defaultRightPadding
    }

    private fun getRemainingScreenSize() =
        (DeviceScreenInfo.getScreenWidth(context) * REMAINING_SCREEN_SIZE_PERCENTAGE).toInt()

    private fun getIsLastItem(viewType: Int, absolutePosition: Int, itemCount: Int) =
        if (viewType == ProductCardListViewHolder.LAYOUT) absolutePosition >= itemCount - itemPerPage
        else absolutePosition == itemCount - 1

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val drawable = drawable ?: return
        canvas.save()

        (0 until parent.childCount).forEach { index ->
            if (index % itemPerPage < (itemPerPage - 1)) {
                val view = parent.getChildAt(index)

                if (view is ProductCardListView) {
                    drawable.apply {
                        val left = view.left + 8.toPx()
                        val top = view.bottom + 8.toPx()
                        val right = view.right - 8.toPx()
                        val bottom = view.bottom + 9.toPx()
                        bounds = Rect(left, top, right, bottom)
                        draw(canvas)
                    }
                }
            }
        }
        canvas.restore()
    }

    companion object {
        private const val REMAINING_SCREEN_SIZE_PERCENTAGE = 0.2
        private const val BOTTOM_PADDING_DP = 1

        fun createDrawable(context: Context): Drawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE

                val colorRes = com.tokopedia.unifycomponents.R.color.Unify_NN50
                setColor(ContextCompat.getColor(context, colorRes))
                setSize(1.toPx(), 1.toPx())
            }
        }
    }
}
