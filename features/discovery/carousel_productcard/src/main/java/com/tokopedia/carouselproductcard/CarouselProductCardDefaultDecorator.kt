package com.tokopedia.carouselproductcard

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.R

internal class CarouselProductCardDefaultDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = view.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = view.context.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
        }
    }
}
