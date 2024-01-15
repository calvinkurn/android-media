package com.tokopedia.carouselproductcard

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.IProductCardView
import com.tokopedia.unifycomponents.toPx

internal class CarouselProductCardDefaultDecorator(
    private val isReimagine: Boolean,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        val additionalMarginStart = additionalMarginStart(view)

        val isFirstItem = parent.getChildAdapterPosition(view) == 0
        val isLastItem = itemCount > 0 && itemPosition == itemCount - 1

        outRect.left = getLeftOffset(isFirstItem, additionalMarginStart)
        outRect.right = getRightOffset(isLastItem)
    }

    private fun additionalMarginStart(view: View): Int =
        if (isReimagine && view is IProductCardView) view.additionalMarginStart
        else 0

    private fun getLeftOffset(isFirstItem: Boolean, additionalMarginStart: Int) =
        (if (isFirstItem) 16.toPx() else marginBetweenCard()) - additionalMarginStart

    private fun getRightOffset(isLastItem: Boolean) =
        if (isLastItem) 16.toPx() else marginBetweenCard()

    private fun marginBetweenCard() = if (isReimagine) 4.toPx() else 0
}
