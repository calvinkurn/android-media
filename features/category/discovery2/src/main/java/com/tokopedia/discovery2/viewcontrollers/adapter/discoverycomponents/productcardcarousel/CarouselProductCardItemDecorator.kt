package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R

internal class CarouselProductCardItemDecorator(val dimen : Int? = null) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return
        val itemCount = state.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = dimen ?: view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.right = dimen ?: view.context.resources.getDimensionPixelSize(R.dimen.dp_16)
        }
    }
}